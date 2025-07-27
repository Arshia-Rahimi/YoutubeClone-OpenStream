package com.github.openstream.core.media3

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.github.openstream.core.common.util.Logger
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.VideoData
import com.github.openstream.core.shared.extractor.data.VideoOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val SEEK_INCREMENT = 10_000L

@OptIn(UnstableApi::class)
class OpenStreamMediaPlayer(
    private val context: Context,
    private val videoRepo: VideoRepository,
    private val scope: CoroutineScope,
    private val logger: Logger,
) {
    private val mainThreadScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
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
        })
    }

    sealed interface FetchingState {
        data object Loading : FetchingState
        data class Success(val video: VideoData) : FetchingState
        data class Error(val videoItem: VideoItem, val message: String? = null) : FetchingState
    }

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering = _isBuffering.asStateFlow()

    private val _fetchingState: MutableStateFlow<FetchingState> =
        MutableStateFlow(FetchingState.Loading)
    val fetchingState = _fetchingState.asStateFlow()

    private val _currentQuality: MutableStateFlow<VideoOption?> = MutableStateFlow(null)
    val currentQuality = _currentQuality.asStateFlow()

    private val _isAudioOnlyModeEnabled = MutableStateFlow(false)
    val isAudioOnlyModeEnabled = _isAudioOnlyModeEnabled.asStateFlow()

    private val _isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    val playerPosition = isPlaying.transform { isPlaying ->
        emit(player.currentPosition / 1000)
        while (isPlaying) {
            emit(player.currentPosition / 1000)
            delay(1000L)
        }
    }

    fun start(video: VideoItem) {
        logger.i(this::class.simpleName, "start player")
        scope.launch {
            withContext(Dispatchers.Main) {
                player.pause()
                player.clearMediaItems()
            }
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
                            if (_isAudioOnlyModeEnabled.value) {
                                player.setMediaItem(it.data.getAudioOnlyMediaItem())
                            } else {
                                player.setMediaSource(it.data.getMediaSource())
                            }
                            player.prepare()
                            player.play()
                        }
                    }
                }
            }
        }
    }

    fun retry() {
        when (_fetchingState.value) {
            is FetchingState.Error -> (_fetchingState.value as FetchingState.Error).videoItem
            else -> return
        }.let { start(it) }
    }

    fun clear() {
        logger.i(this::class.simpleName, "clear player")
        mainThreadScope.launch {
            player.pause()
            player.clearMediaItems()
        }
    }

    fun resume() {
        mainThreadScope.launch { player.play() }
    }

    fun pause() {
        mainThreadScope.launch { player.pause() }
    }

    fun seekTo(ms: Long) {
        mainThreadScope.launch { player.seekTo(ms) }
    }

    fun seekForward() {
        mainThreadScope.launch { player.seekTo(playerPosition.first() + SEEK_INCREMENT) }
    }

    fun seekBackward() {
        mainThreadScope.launch {
            player.seekTo(
                (playerPosition.first() - SEEK_INCREMENT).coerceAtLeast(
                    0L
                )
            )
        }
    }

    fun switchPlaybackQuality(videoOption: VideoOption) {
        val videoData = when (fetchingState.value) {
            is FetchingState.Success -> (fetchingState.value as FetchingState.Success).video
            else -> return
        }
        if(isAudioOnlyModeEnabled.value) return
        
        mainThreadScope.launch {
            _currentQuality.value = videoOption

            val wasPlaying = player.isPlaying
            player.pause()
            
            val currentPosition = player.currentPosition
            val mediaSource = videoData.getMediaSource(videoOption)
            player.setMediaSource(mediaSource)
            player.prepare()
            player.seekTo(currentPosition)

            if (wasPlaying) player.play()
            logger.i(this::class.simpleName, "switched playback quality")
        }
    }

    fun toggleAudioOnlyMode() {
        val videoData = when (fetchingState.value) {
            is FetchingState.Success -> (fetchingState.value as FetchingState.Success).video
            else -> return
        }

        val isAudioOnly = !_isAudioOnlyModeEnabled.value
        _isAudioOnlyModeEnabled.value = isAudioOnly

        val currentQuality = _currentQuality.value ?: return
        val currentPosition = player.currentPosition

        val wasPlaying = player.isPlaying
        player.pause()

        if (isAudioOnly) {
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
