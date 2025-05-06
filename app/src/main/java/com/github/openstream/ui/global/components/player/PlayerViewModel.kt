package com.github.openstream.ui.global.components.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.github.openstream.app.MainActivity
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.media3.PlayingStatus
import com.github.openstream.core.model.extractordata.VideoData
import com.github.openstream.ui.global.components.player.components.PlayerSheetState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
class PlayerViewModel(
    private val player: OpenStreamMediaPlayer,
    private val videoRepository: VideoRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val data: VideoData) : UiState
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    val dragState = AnchoredDraggableState(
        initialValue = PlayerSheetState.MINI_PLAYER,
//        anchors = DraggableAnchors {
//            PlayerSheetState.MINI_PLAYER at 1f
//            PlayerSheetState.EXPANDED at 0f
//        },
//        positionalThreshold = { distance: Float -> distance * 0.01f },
//        velocityThreshold = { 100f },
//        decayAnimationSpec = exponentialDecay(),
//        snapAnimationSpec = spring(
//            stiffness = Spring.StiffnessLow
//        ),
    )

    private val _showMiniPlayer = MutableStateFlow(false)
        .apply {
            onEach { if (it) player.init() else player.release() }
                .launchIn(viewModelScope)
        }
    val showMiniPlayer = _showMiniPlayer.asStateFlow()
    val sheetState = snapshotFlow { dragState.settledValue }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlayerSheetState.MINI_PLAYER)
    val isInLandscape = MainActivity.isInLandScape.asStateFlow()
    val shouldShowFullscreenPlayer =
        combine(
            showMiniPlayer,
            sheetState,
            isInLandscape
        ) { showMiniPlayer, sheetState, isInLandscape ->
            showMiniPlayer && (sheetState == PlayerSheetState.EXPANDED) && isInLandscape
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val viewPlayer: Player
        get() = player.player
    val playerState = player.playerState
    val currentPosition = player.playerPosition
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0L)

    fun start(videoUrl: String) {
        if (!_showMiniPlayer.value) _showMiniPlayer.value = true
        player.pause()
        viewModelScope.launch {
            videoRepository.fetchVideo(videoUrl)
                .collect { video ->
                    _uiState.value = when (video) {
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
