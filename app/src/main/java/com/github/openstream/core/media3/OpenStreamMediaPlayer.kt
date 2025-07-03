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

    val player: ExoPlayer = ExoPlayer.Builder(context).build()
        .apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }
            })

            playerDataRepo.playerData.onEach {
                player.setSeekParameters(SeekParameters(it.seekIncrement, it.seekIncrement))
                player.setPlaybackSpeed(it.playbackSpeed)
            }.launchIn(mainThreadScope)
        }

    sealed interface FetchingState {
        data object Loading : FetchingState
        data object Success : FetchingState
        data class Error(val message: String? = null) : FetchingState
    }

    val playerState = playerDataRepo.playerData
    val currentVideo = playerState.map { it.currentVideoIndex?.let { index -> it.queue[index] } }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)
    
    private val _currentVideoData: MutableStateFlow<VideoData?> = MutableStateFlow(null)
    val currentVideoData = _currentVideoData.asStateFlow()

    private val _fetchingState: MutableStateFlow<FetchingState> =
        MutableStateFlow(FetchingState.Loading)
    val fetchingState = _fetchingState.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()
    
    val playerPosition = isPlaying.transform {
        if (it) {
            while (true) {
                emit(player.currentPosition / 1000)
                delay(1000L)
            }
        } else {
            emit(player.currentPosition / 1000)
        }
    }

    private fun fetchAndPlayVideo(video: VideoItem) {
        mainThreadScope.launch {
            _fetchingState.value = FetchingState.Loading
            player.pause()
            player.clearMediaItems()

            val mediaItem = withContext(Dispatchers.IO) {
                videoRepo.fetchVideo(video.url)
            }

            if (mediaItem is Resource.Error) {
                _fetchingState.value = FetchingState.Error(mediaItem.message)
                return@launch
            }

            mediaItem as Resource.Success
            player.setMediaItem(mediaItem.data)
            player.prepare()
            _fetchingState.value = FetchingState.Success
            player.play()
        }
    }

    fun start(videos: List<VideoItem>, index: Int) {
        scope.launch {
            playerDataRepo.replaceQueue(videos, index) 
        }
    }

    fun clear() {
        pause()
        player.clearMediaItems()
        scope.launch { playerDataRepo.clearQueue() }
    }

    fun resume() {
        mainThreadScope.launch { player.play() }
        _isPlaying.value = true
    }

    fun pause() {
        mainThreadScope.launch { player.pause() }
        _isPlaying.value = false
    }

    fun toggleIsPlaying() =
        if (player.isPlaying) player.pause()
        else player.play()

    fun seekTo(ms: Long) = player.seekTo(ms)

    fun seekForward() = player.seekForward()

    fun seekBackward() = player.seekBack()

    fun setPlaybackSpeed(speed: Float) {
        scope.launch { playerDataRepo.setPlaybackSpeed(speed) }
    }

    fun toggleShuffleMode() = scope.launch { playerDataRepo.toggleShuffleMode() }

    fun changeRepeatMode() = scope.launch { playerDataRepo.changeRepeatMode() }

}
