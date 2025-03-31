package com.github.freetube.ui.global.player

import androidx.compose.runtime.Composable
import androidx.media3.common.Player
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.VideoRepository
import com.github.freetube.core.extractor.video.VideoData
import com.github.freetube.core.media3.LibreTubeMediaPlayer
import com.github.freetube.core.media3.PlayingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerScreenModel(
    private val player: LibreTubeMediaPlayer,
    private val videoRepository: VideoRepository,
) : StateScreenModel<PlayerScreenModel.UiState>(UiState.Loading) {
    //    val scrollBehavior     
    //    @Composable get() = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val topBar: MutableStateFlow<(@Composable () -> Unit)> = MutableStateFlow({})

    private val _showMiniPlayer = MutableStateFlow(false)
    val showMiniPlayer = _showMiniPlayer.asStateFlow()
        .apply {
            onEach { if (it) player.init() else player.release() }
                .launchIn(screenModelScope)
        }

    val viewPlayer: Player
        get() = player.player
    val playerState = player.playerState
    val currentPosition = player.playerPosition
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000L), 0L)

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val data: VideoData) : UiState
    }

    fun start(videoUrl: String) {
        if (!_showMiniPlayer.value) _showMiniPlayer.value = true
        screenModelScope.launch {
            videoRepository.fetchVideo(videoUrl)
                .collect { video ->
                    mutableState.value = when (video) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> UiState.Error(video.message)
                        is Resource.Success -> {
                            UiState.Success(video.data.localConfiguration?.tag as VideoData)
                                .also {
                                    player.prepareSingleVideo(video.data)
                                    player.resume()
                                }
                        }
                    }
                }
        }
    }

    fun togglePlay() {
        when (playerState.value.playingStatus) {
            PlayingStatus.PLAYING -> player.pause()
            PlayingStatus.PAUSED -> player.resume()
            else -> Unit
        }
    }

    fun dispose() {
        _showMiniPlayer.value = false
    }
}
