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
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.log.Logger
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(UnstableApi::class)
class OpenStreamMediaPlayer(
    private val context: Context,
    private val videoRepo: VideoRepository,
    private val scope: CoroutineScope,
    private val logger: Logger,
) {
    private val mainThreadScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var isManuallyChangingMedia = true
    val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(object : Player.Listener {
            private var currentMediaItemIndex = -1
            
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED && !isManuallyChangingMedia) {
                    this@OpenStreamMediaPlayer.next(true)
                }
            }
            
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                logger.log(error.localizedMessage ?: "player error")
                println(error.localizedMessage ?: "error")
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                currentMediaItemIndex = (mediaItem?.mediaMetadata?.extras?.getInt("index") ?: -1)
            }
        })
    }

    val queue = MutableStateFlow<List<VideoItem>>(emptyList())

    private val _currentVideo = MutableStateFlow<VideoItem?>(null)
        .apply {
            onEach { currentVideo ->
                println("video: $currentVideo")
                if (currentVideo == null) return@onEach
                if (currentVideoData.value?.url == currentVideo.url) return@onEach

                fetchVideo(currentVideo)
            }.launchIn(scope)
        }
    val currentVideo = _currentVideo.asStateFlow()

    sealed interface FetchingState {
        data object Loading : FetchingState
        data object Success : FetchingState
        data class Error(val message: String? = null) : FetchingState
    }

    private val _fetchingState: MutableStateFlow<FetchingState> =
        MutableStateFlow(FetchingState.Loading)
    val fetchingState = _fetchingState.asStateFlow()

    private val _currentQuality: MutableStateFlow<VideoOption?> = MutableStateFlow(null)
    val currentQuality = _currentQuality.asStateFlow()
    
    private val _isAudioOnlyModeEnabled = MutableStateFlow(false)
    val isAudioOnlyModeEnabled = _isAudioOnlyModeEnabled.asStateFlow()
    
    private val _currentVideoData: MutableStateFlow<VideoData?> = MutableStateFlow(null)
    val currentVideoData = _currentVideoData.asStateFlow()

    private val _isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    val playerPosition = isPlaying.transform { isPlaying ->
        emit(player.currentPosition / 1000)
        while (isPlaying) {
            emit(player.currentPosition / 1000)
            delay(1000L)
        }
    }
    
    fun switchPlaybackQuality(videoOption: VideoOption) {
        mainThreadScope.launch {
            _currentQuality.value = videoOption
            
            val currentVideoData = _currentVideoData.value ?: return@launch
            val wasPlaying = isPlaying.value
            if (wasPlaying) pause()

            val currentPosition = player.currentPosition
            val mediaSource = currentVideoData.getMediaSource(videoOption)
            player.setMediaSource(mediaSource)
            player.prepare()
            player.seekTo(currentPosition)

            if (wasPlaying) resume()
        }
    }
    
    fun toggleAudioOnlyMode() {
        val currentVideoData = _currentVideoData.value ?: return
        val currentQuality = _currentQuality.value ?: return
        val currentPosition = player.currentPosition
        
        val wasPlaying = isPlaying.value
        if (wasPlaying) pause()
        
        val isAudioOnly = _isAudioOnlyModeEnabled.value
        _isAudioOnlyModeEnabled.value = !isAudioOnly
        
        if (isAudioOnly) {
            player.setMediaItem(currentVideoData.getAudioOnlyMediaItem())
        } else {
            player.setMediaSource(currentVideoData.getMediaSource(currentQuality))
        }
        
        player.prepare()
        player.seekTo(currentPosition)
        if (wasPlaying) resume()
    }

    fun start(videos: List<VideoItem>, videoItem: VideoItem) {
        queue.value = videos
        _currentVideo.value = videoItem
        resume()
    }

    fun clear() {
        pause()
        player.clearMediaItems()
        queue.value = emptyList()
        _currentVideoData.value = null
    }

    fun resume() {
        mainThreadScope.launch { player.play() }
        _isPlaying.value = true
    }

    fun pause() {
        mainThreadScope.launch { player.pause() }
        _isPlaying.value = false
    }

    fun toggleIsPlaying() = if (player.isPlaying) player.pause() else player.play()

    fun next(isCalledByListener: Boolean = false) {
        isManuallyChangingMedia = !isCalledByListener
        val currentVideoIndex = queue.value.indexOf(_currentVideo.value)
        println(currentVideoIndex)
        if(currentVideoIndex == -1) return
        val nextVideoIndex = currentVideoIndex+1
        _currentVideo.value = queue.value[nextVideoIndex]
    }

    suspend fun previous() = withContext(Dispatchers.Main) {
        if (playerPosition.first() >= 5000) {
            seekTo(0)
            return@withContext
        }

        val currentVideoIndex = queue.value.indexOf(_currentVideo.value)
        if(currentVideoIndex == -1) return@withContext
        
        val previousVideoIndex = if(currentVideoIndex == 0) queue.value.lastIndex else currentVideoIndex-1
        _currentVideo.value = queue.value[previousVideoIndex]
    }

    fun playerFromVideo(videoItem: VideoItem) {
        if(videoItem !in queue.value) return
        _currentVideo.value = videoItem
    }

    fun seekTo(ms: Long) = player.seekTo(ms)

    fun seekForward() = player.seekForward()

    fun seekBackward() = player.seekBack()
    
    fun playNext(video: VideoItem) {
        val currentVideo = currentVideo.value ?: return
        val newQueue = queue.value.toMutableList()
        val currentVideoIndex = newQueue.indexOf(currentVideo)
        if (currentVideoIndex == -1) return
        
        newQueue.add(currentVideoIndex + 1, video)
        queue.value = newQueue
    }

    private suspend fun fetchVideo(video: VideoItem) {
        withContext(Dispatchers.Main) {
            player.pause()
            player.clearMediaItems()
        }
        videoRepo.fetchVideo(video.url).collect {
            when (it) {
                is Resource.Loading -> _fetchingState.value = FetchingState.Loading
                is Resource.Error -> _fetchingState.value = FetchingState.Error(it.message)
                is Resource.Success -> {
                    _currentVideoData.value = it.data
                    
                    withContext(Dispatchers.Main) {
                        if (_isAudioOnlyModeEnabled.value) {
                            player.setMediaItem(it.data.getAudioOnlyMediaItem())
                        } else {
                            _currentQuality.value = _currentVideoData.value?.videoOptions?.last()
                            _currentQuality.value?.let { videoOption -> player.setMediaSource(it.data.getMediaSource(videoOption)) }
                        }
                        player.prepare()
                        if (isPlaying.value) resume()
                        isManuallyChangingMedia = false
                    }
                    
                    _fetchingState.value = FetchingState.Success
                }
            }
        }
    }

    private fun VideoData.getMediaSource(videoOption: VideoOption): MediaSource {
        val videoItem = MediaItem.Builder().setUri(videoOption.content).build()
        val audioItem = MediaItem.Builder().setUri(audioStream).build()
        val dataSourceFactory = DefaultDataSource.Factory(context)

        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(videoItem)
        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(audioItem)

        return MergingMediaSource(true, true, videoSource, audioSource)
    }
    
    private fun VideoData.getAudioOnlyMediaItem() =
        MediaItem.Builder().setUri(url).build()

}
