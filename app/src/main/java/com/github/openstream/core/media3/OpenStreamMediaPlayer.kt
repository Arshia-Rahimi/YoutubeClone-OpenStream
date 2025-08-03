package com.github.openstream.core.media3

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.session.MediaSession
import com.github.openstream.core.common.util.Logger
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PreferencesRepository
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.VideoData
import com.github.openstream.core.shared.extractor.data.VideoOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val SEEK_INCREMENT = 10_000L

@OptIn(UnstableApi::class)
class OpenStreamMediaPlayer(
    private val context: Context,
    private val videoRepo: VideoRepository,
    private val preferencesRepo: PreferencesRepository,
    private val scope: CoroutineScope,
    private val logger: Logger,
) {
    sealed interface FetchingState {
        data object Loading : FetchingState
        data class Success(val video: VideoData) : FetchingState
        data class Error(val videoItem: VideoItem, val message: String? = null) : FetchingState
    }
    
    val mainThreadScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build(),
            true,
        )
        
        repeatMode = Player.REPEAT_MODE_ALL
        
        addListener(object : Player.Listener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                super.onPlayWhenReadyChanged(playWhenReady, reason)
                _isPlaying.value = playWhenReady
            }
            
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                _isBuffering.value = playbackState == Player.STATE_BUFFERING
            }
            
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                logger.e("OpenStreamMediaPlayer", "player error", error)
            }
            
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                super.onPlaybackParametersChanged(playbackParameters)
                _playbackSpeed.value = playbackParameters.speed
            }
        })
    }
    
    private var fetchJob: Job? = null
    
    private val _isBuffering = MutableStateFlow(false)
    val isBuffering = _isBuffering.asStateFlow()
    
    private val _fetchingState: MutableStateFlow<FetchingState> =
        MutableStateFlow(FetchingState.Loading)
    val fetchingState = _fetchingState.asStateFlow()
    
    private val _currentQuality: MutableStateFlow<VideoOption?> = MutableStateFlow(null)
    val currentQuality = _currentQuality.asStateFlow()
    
    val isAudioOnlyModeEnabled = preferencesRepo.preferences
        .map { it.isAudioOnlyModeEnabled }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), false)
        .apply {
            onEach {
                switchAudioOnlyMode(it)
            }.launchIn(scope)
        }
    
    private val _isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()
    
    val playerPosition = isPlaying.transform { isPlaying ->
        emit(player.currentPosition)
        while (isPlaying) {
            emit(player.currentPosition)
            delay(500L)
        }
    }.stateIn(mainThreadScope, SharingStarted.WhileSubscribed(5000), 0L)
    
    val bufferedPosition = flow {
        while (true) {
            emit(player.bufferedPosition)
            delay(500L)
        }
    }.stateIn(mainThreadScope, SharingStarted.WhileSubscribed(5000), 0L)
    
    private val _playbackSpeed = MutableStateFlow(1f)
    val playbackSpeed = _playbackSpeed.asStateFlow()
    
    init {
        MediaSession.Builder(context, player).build()
    }
    
    fun start(video: VideoItem) {
        logger.i(this::class.simpleName, "start player")
        clear()
        fetchJob?.cancel()
        fetchJob = scope.launch {
            videoRepo.fetchVideo(video.url).collect {
                when (it) {
                    is Resource.Loading -> _fetchingState.value = FetchingState.Loading
                    is Resource.Error -> {
                        logger.e("OpenStreamMediaPlayer", "error in fetching video", it.error)
                        _fetchingState.value =
                            FetchingState.Error(video, it.error?.localizedMessage ?: "")
                    }
                    
                    is Resource.Success -> {
                        _fetchingState.value = FetchingState.Success(it.data)
                        withContext(Dispatchers.Main) {
                            if (isAudioOnlyModeEnabled.value) {
                                player.setMediaItem(it.data.getAudioOnlyMediaItem())
                            } else {
                                player.setMediaSource(it.data.getMediaSource())
                            }
                            player.prepare()
                            player.seekTo(it.data.position)
                            player.play()
                        }
                    }
                }
            }
        }
    }
    
    fun retry() {
        when (_fetchingState.value) {
            is FetchingState.Error -> (_fetchingState.value as FetchingState.Error).videoItem.let(::start)
            else -> return
        }
    }
    
    fun clear() {
        logger.i(this::class.simpleName, "clear player")
        fetchJob?.cancel()
        player.pause()
        val position = playerPosition.value
        when (val state = fetchingState.value) {
            is FetchingState.Success -> scope.launch {
                videoRepo.saveVideo(
                    state.video.toDataItem().copy(position = position)
                )
            }
            
            else -> Unit
        }
        player.clearMediaItems()
    }
    
    fun destroy() {
        player.release()
    }
    
    fun setPlaybackSpeed(speed: Float) {
        player.setPlaybackSpeed(speed)
    }
    
    fun resume() {
        player.play()
    }
    
    fun pause() {
        player.pause()
    }
    
    fun seekTo(ms: Long) {
        player.seekTo(ms)
    }
    
    fun seekForward() {
        player.seekTo(player.currentPosition + SEEK_INCREMENT)
    }
    
    fun seekBackward() {
        player.seekTo(
            (player.currentPosition - SEEK_INCREMENT).coerceAtLeast(0L)
        )
    }
    
    fun switchPlaybackQuality(videoOption: VideoOption) {
        val videoData = when (fetchingState.value) {
            is FetchingState.Success -> (fetchingState.value as FetchingState.Success).video
            else -> return
        }
        if (isAudioOnlyModeEnabled.value) return
        
        _currentQuality.value = videoOption
        
        val wasPlaying = player.isPlaying
        
        val currentPosition = player.currentPosition
        val mediaSource = videoData.getMediaSource(videoOption)
        
        player.pause()
        player.clearMediaItems()
        player.setMediaSource(mediaSource)
        player.prepare()
        player.seekTo(currentPosition)
        
        if (wasPlaying) player.play()
        logger.i(this::class.simpleName, "switched playback quality")
    }
    
    fun toggleAudioOnlyMode() {
        scope.launch {
            val isAudioOnly = !isAudioOnlyModeEnabled.value
            preferencesRepo.setAudioOnlyMode(isAudioOnly)
        }
    }
    
    private suspend fun switchAudioOnlyMode(enabled: Boolean) = withContext(Dispatchers.IO) {
        val videoData = when (fetchingState.value) {
            is FetchingState.Success -> (fetchingState.value as FetchingState.Success).video
            else -> return@withContext
        }
        
        val currentQuality = _currentQuality.value ?: return@withContext
        val currentPosition = player.currentPosition
        val wasPlaying = player.isPlaying
        
        player.pause()
        player.clearMediaItems()
        
        if (enabled) {
            logger.i(this::class.simpleName, "switch to audio only")
            player.setMediaItem(videoData.getAudioOnlyMediaItem())
        } else {
            logger.i(this::class.simpleName, "switch to video and audio")
            player.setMediaSource(videoData.getMediaSource(currentQuality))
        }
        
        player.prepare()
        player.seekTo(currentPosition)
        if (wasPlaying) player.play()
    }
    
    private fun VideoData.getMediaSource(videoOption: VideoOption? = null): MediaSource {
        val option = videoOption ?: this.videoOptions.last()
        _currentQuality.value = option
        
        val videoItem = MediaItem.Builder().setUri(option.content).build()
        val audioItem = MediaItem.Builder().setUri(audioStream).build()
        val dataSourceFactory = DefaultDataSource.Factory(context)
        
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(videoItem)
        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(audioItem)
        
        return MergingMediaSource(true, true, videoSource, audioSource)
    }
    
    private fun VideoData.getAudioOnlyMediaItem() =
        MediaItem.Builder().setUri(audioStream).build()
    
}
