package com.github.freetube.ui.global.player

import androidx.media3.common.MediaItem
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.VideoRepository
import com.github.freetube.core.media3.LibreTubeMediaPlayer
import com.github.freetube.core.media3.PlayingStatus
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerScreenModel(
    private val player: LibreTubeMediaPlayer,
    private val videoRepository: VideoRepository,
) : StateScreenModel<PlayerScreenModel.UiState>(UiState.Loading) {

    private val _showMiniPlayer = MutableStateFlow(false)
    val showMiniPlayer = _showMiniPlayer.asStateFlow()

    val viewPlayer = player.player
    val playerState = player.playerState
    val currentVideo = player.currentVideo
    val currentPosition = player.playerPosition
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000L), 0L)

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val data: MediaItem) : UiState
    }

    fun start(videoUrl: String) {
        screenModelScope.launch {
            val videoData = async {
                videoRepository.fetchVideo(videoUrl)
                    .collect {
                        mutableState.value = when (it) {
                            is Resource.Loading -> UiState.Loading
                            is Resource.Error -> UiState.Error(it.message)
                            is Resource.Success -> {
                                player.init()
                                UiState.Success(it.data)
                            }
                        }
                    }
            }
            _showMiniPlayer.value = true
            player.init()
//            player.prepareSingleVideo(videoData.await())
        }
    }

    fun togglePlay() {
        playerState.value?.let {
            when (it.playingStatus) {
                PlayingStatus.PLAYING -> player.pause()
                PlayingStatus.PAUSED -> player.resume()
                else -> Unit
            }
        }
    }
}
