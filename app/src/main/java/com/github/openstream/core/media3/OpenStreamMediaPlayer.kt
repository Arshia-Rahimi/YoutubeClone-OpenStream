package com.github.openstream.core.media3

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlayerDataRepository
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.model.dataitem.VideoItem
import com.github.openstream.core.model.extractordata.VideoData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(UnstableApi::class)
class OpenStreamMediaPlayer(
    context: Context,
    private val videoRepo: VideoRepository,
    private val playerDataRepo: PlayerDataRepository,
    private val scope: CoroutineScope,
) {
    private val mainThreadScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState != Player.STATE_ENDED) return
                val playerRepeatMode = this@OpenStreamMediaPlayer.repeatMode.value
                if (playerRepeatMode == PlayerRepeatMode.ONE) return

                val currentVideoIndex = queue.value.indexOf(_currentVideo.value)
                if (currentVideoIndex == -1) return

                var nextVideoIndex = currentVideoIndex + 1
                if (nextVideoIndex >= queue.value.size) {
                    nextVideoIndex =
                        if (playerRepeatMode == PlayerRepeatMode.ALL) 0
                        else currentVideoIndex
                }

                _currentVideo.value = queue.value[nextVideoIndex]
            }
        })
    }

    val queue = MutableStateFlow<List<VideoItem>>(emptyList())

    private val _currentVideo = MutableStateFlow<VideoItem?>(null)
        .apply {
            onEach { currentVideo ->
                if (currentVideo == null) return@onEach
                if (currentVideoData.value?.toDataItem() == currentVideo) return@onEach

                fetchVideo(currentVideo)
                if (isPlaying.value) resume()

            }.launchIn(scope)
        }
    val currentVideo = _currentVideo.asStateFlow()

    val playbackSpeed = playerDataRepo.playerData
        .map { it.playbackSpeed }.stateIn(scope, SharingStarted.WhileSubscribed(5000), 1f)
        .apply { onEach { player.setPlaybackSpeed(it) }.launchIn(mainThreadScope) }

    val repeatMode = playerDataRepo.playerData
        .map { it.repeatMode }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), PlayerRepeatMode.ALL)
        .apply {
            onEach {
                player.repeatMode =
                    if (it == PlayerRepeatMode.ONE) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
            }.launchIn(mainThreadScope)
        }
    
    private val seekIncrement = playerDataRepo.playerData
        .map { it.seekIncrement }.stateIn(scope, SharingStarted.WhileSubscribed(5000), 10000L)
        .apply { onEach { player.setSeekParameters(SeekParameters(it, it)) }.launchIn(mainThreadScope) }


    sealed interface FetchingState {
        data object Loading : FetchingState
        data object Success : FetchingState
        data class Error(val message: String? = null) : FetchingState
    }

    private val _fetchingState: MutableStateFlow<FetchingState> =
        MutableStateFlow(FetchingState.Loading)
    val fetchingState = _fetchingState.asStateFlow()

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

    fun start(videos: List<VideoItem>, index: Int) {
        queue.value = videos
        _currentVideo.value = videos[index]
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

    fun next() {
        val currentVideoIndex = queue.value.indexOf(_currentVideo.value)
        if (currentVideoIndex == -1) return

        var nextVideoIndex = currentVideoIndex + 1
        if (nextVideoIndex >= queue.value.size) {
            if (repeatMode.value != PlayerRepeatMode.ALL) return
            nextVideoIndex = 0
        }

        _currentVideo.value = queue.value[nextVideoIndex]
    }

    fun previous() {
        val currentVideoIndex = queue.value.indexOf(_currentVideo.value)
        if (currentVideoIndex == -1) return

        val previousVideoIndex = currentVideoIndex - 1
        if (previousVideoIndex < 0) {
            player.seekTo(0)
            return
        }

        _currentVideo.value = queue.value[previousVideoIndex]
    }

    fun seekTo(ms: Long) = player.seekTo(ms)

    fun seekForward() = player.seekForward()

    fun seekBackward() = player.seekBack()

    fun setPlaybackSpeed(speed: Float) {
        scope.launch { playerDataRepo.setPlaybackSpeed(speed) }
    }

    fun changeRepeatMode() = scope.launch { playerDataRepo.changeRepeatMode() }

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
                        player.setMediaItem(it.data.getMediaItem(it.data.videoStreams.first().content))
                        player.prepare()
                    }
                    _fetchingState.value = FetchingState.Success
                }
            }
        }
    }

}
