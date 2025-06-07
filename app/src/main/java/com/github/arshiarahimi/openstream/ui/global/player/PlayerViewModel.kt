package com.github.arshiarahimi.openstream.ui.global.player

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.app.MainActivity
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.core.data.VideoRepository
import com.github.arshiarahimi.openstream.core.media3.OpenStreamMediaPlayer
import com.github.arshiarahimi.openstream.core.media3.PlayingStatus
import com.github.arshiarahimi.openstream.core.model.extractordata.VideoData
import com.github.arshiarahimi.openstream.core.shared.DefaultPlaylists
import com.github.arshiarahimi.openstream.ui.global.player.components.PlayerSheetState
import com.github.arshiarahimi.openstream.ui.global.player.components.VideoPlaylistsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModel(
    private val player: OpenStreamMediaPlayer,
    private val videoRepo: VideoRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val data: VideoData) : UiState
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    val playlistsState = _uiState.flatMapLatest {
        if (it !is UiState.Success || it.data.id == null)
            return@flatMapLatest flow { emit(VideoPlaylistsState()) }

        combine(
            playlistRepo.isInPlaylist(it.data.id, DefaultPlaylists.WATCH_LATER_ID),
            playlistRepo.isInPlaylist(it.data.id, DefaultPlaylists.LIKED_VIDEOS_ID)
        ) { isInWatchLater, isLiked ->
            VideoPlaylistsState(isInWatchLater, isLiked)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VideoPlaylistsState())
    
    val viewPlayer = player.player

    val dragState = AnchoredDraggableState(PlayerSheetState.MINI_PLAYER)

    private val _showMiniPlayer = MutableStateFlow(false)
    val showMiniPlayer = _showMiniPlayer.asStateFlow()
    val sheetState = snapshotFlow { dragState.settledValue }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlayerSheetState.MINI_PLAYER)
    val shouldShowFullscreenPlayer =
        combine(
            showMiniPlayer,
            sheetState,
        ) { showMiniPlayer, sheetState ->
            showMiniPlayer && (sheetState == PlayerSheetState.EXPANDED) && MainActivity.isInLandScape
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val playerState = player.playerState
    val currentPosition = player.playerPosition
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0L)
    
    fun processAction(action: PlayerAction) {
        when (action) {
            is PlayerAction.Start -> start(action.url)
            is PlayerAction.TogglePlay -> togglePlay()
        }
    }
    
    private fun start(videoUrl: String) {
        if (!_showMiniPlayer.value) _showMiniPlayer.value = true
        player.pause()
        videoRepo.fetchVideo(videoUrl)
            .onEach { video ->
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
            }.launchIn(viewModelScope)
    }
    
    private fun togglePlay() {
        when (playerState.value.playingStatus) {
            PlayingStatus.PLAYING -> player.pause()
            PlayingStatus.PAUSED -> player.resume()
            else -> Unit
        }
    }

    fun dispose() {
        _showMiniPlayer.value = false
    }

    fun toggleVideoWatchLater() {
        if (_uiState.value !is UiState.Success) return
        val videoItem = (_uiState.value as UiState.Success).data.toDataItem()

        when (playlistsState.value.isInWatchLater) {
            true -> playlistRepo.removeFromPlaylist(
                listOf(videoItem),
                DefaultPlaylists.WATCH_LATER_ID
            )

            false -> playlistRepo.addToPlaylist(listOf(videoItem), DefaultPlaylists.WATCH_LATER_ID)
        }.launchIn(viewModelScope)
    }

    fun toggleVideoLiked() {
        if (_uiState.value !is UiState.Success) return
        val videoItem = (_uiState.value as UiState.Success).data.toDataItem()

        when (playlistsState.value.isLiked) {
            true -> playlistRepo.removeFromPlaylist(
                listOf(videoItem),
                DefaultPlaylists.LIKED_VIDEOS_ID
            )

            false -> playlistRepo.addToPlaylist(listOf(videoItem), DefaultPlaylists.LIKED_VIDEOS_ID)
        }.launchIn(viewModelScope)
    }
}
