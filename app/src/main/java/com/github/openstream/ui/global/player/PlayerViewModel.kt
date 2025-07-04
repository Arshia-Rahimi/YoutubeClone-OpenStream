package com.github.openstream.ui.global.player

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.app.MainActivity
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.model.dataitem.VideoItem
import com.github.openstream.core.shared.DefaultPlaylists
import com.github.openstream.ui.global.player.components.PlayerSheetState
import com.github.openstream.ui.global.player.components.VideoPlaylistsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModel(
    private val player: OpenStreamMediaPlayer,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    val playerInstance = player.player
    
    val fetchingState = player.fetchingState
    val currentVideo = player.currentVideoData
    val playerState = player.playerState
    val isPlaying = player.isPlaying
    val currentPosition = player.playerPosition
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0L)
    
    val playlistsState = fetchingState.flatMapLatest {
        val currentVideoId = currentVideo.value?.id
        if (it !is OpenStreamMediaPlayer.FetchingState.Success || currentVideoId == null)
            flow { emit(VideoPlaylistsState()) }
        else combine(
            playlistRepo.isInPlaylist(currentVideoId, DefaultPlaylists.WATCH_LATER_ID),
            playlistRepo.isInPlaylist(currentVideoId, DefaultPlaylists.LIKED_VIDEOS_ID)
        ) { isInWatchLater, isLiked ->
            VideoPlaylistsState(isInWatchLater, isLiked)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VideoPlaylistsState())

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


    fun processAction(action: PlayerAction) = when (action) {
        is PlayerAction.Start -> start(action.videos, action.index)
        is PlayerAction.SeekTo -> player.seekTo(action.ms)
        is PlayerAction.SetPlaybackSpeed -> player.setPlaybackSpeed(action.speed)
        is PlayerAction.Next -> Unit // todo
        is PlayerAction.Previous -> Unit
        is PlayerAction.SeekBackward -> player.seekBackward()
        is PlayerAction.SeekForward -> player.seekForward()
        is PlayerAction.ToggleShuffleMode -> player.toggleShuffleMode()
        is PlayerAction.ChangeRepeatMode -> player.changeRepeatMode()
        is PlayerAction.TogglePlay -> player.toggleIsPlaying()
    }

    private fun start(videos: List<VideoItem>, index: Int) {
        _showMiniPlayer.value = true
        player.start(videos, index)
    }
    
    fun dispose() {
        player.clear()
        _showMiniPlayer.value = false
    }

    fun toggleVideoWatchLater() {
        if (fetchingState.value != OpenStreamMediaPlayer.FetchingState.Success) return
        val currentVideo = currentVideo.value ?: return

        when (playlistsState.value.isInWatchLater) {
            true -> playlistRepo.removeFromPlaylist(
                listOf(currentVideo.toDataItem()),
                DefaultPlaylists.WATCH_LATER_ID,
            )

            false -> playlistRepo.addToPlaylist(
                listOf(currentVideo.toDataItem()),
                DefaultPlaylists.WATCH_LATER_ID,
            )
        }.launchIn(viewModelScope)
    }

    fun toggleVideoLiked() {
        if (fetchingState.value != OpenStreamMediaPlayer.FetchingState.Success) return
        val currentVideo = currentVideo.value ?: return

        when (playlistsState.value.isLiked) {
            true -> playlistRepo.removeFromPlaylist(
                listOf(currentVideo.toDataItem()),
                DefaultPlaylists.LIKED_VIDEOS_ID,
            )

            false -> playlistRepo.addToPlaylist(
                listOf(currentVideo.toDataItem()),
                DefaultPlaylists.LIKED_VIDEOS_ID,
            )
        }.launchIn(viewModelScope)
    }
}
