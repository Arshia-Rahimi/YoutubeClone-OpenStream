package com.github.openstream.ui.global.player

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.app.MainActivity
import com.github.openstream.core.common.compose.collectToSnapShotStateList
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.shared.DefaultPlaylists
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.VideoOption
import com.github.openstream.ui.global.player.model.PlayerSheetState
import com.github.openstream.ui.global.player.model.VideoLocalState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModel(
    private val player: OpenStreamMediaPlayer,
    private val playlistRepo: PlaylistRepository,
    private val channelRepo: ChannelRepository,
) : ViewModel() {

    val playerInstance = player.player

    val queue = player.queue.collectToSnapShotStateList(viewModelScope)
    val fetchingState = player.fetchingState
    val currentVideoData = player.currentVideoData
    val currentVideo = player.currentVideo
    val currentQuality = player.currentQuality
    val isPlaying = player.isPlaying
    val isAudioOnlyModeEnabled = player.isAudioOnlyModeEnabled
    val currentPosition = player.playerPosition
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)
    
    val playlistsState = fetchingState.flatMapLatest {
        val currentVideoId = currentVideoData.value?.id
        if (it !is OpenStreamMediaPlayer.FetchingState.Success || currentVideoId == null)
            flow { emit(VideoLocalState()) }
        else combine(
            playlistRepo.isInPlaylist(currentVideoId, DefaultPlaylists.WATCH_LATER_ID),
            playlistRepo.isInPlaylist(currentVideoId, DefaultPlaylists.LIKED_VIDEOS_ID),
            channelRepo.isChannelsubscribed(currentVideoData.value?.channelUrl ?: ""),
        ) { isInWatchLater, isLiked, isChannelSubscribed ->
            VideoLocalState(isInWatchLater, isLiked, isChannelSubscribed)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VideoLocalState())

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
        is PlayerAction.PlayFromItem -> player.playerFromVideo(action.video)
        is PlayerAction.Next -> player.next()
        is PlayerAction.Previous -> viewModelScope.launch { player.previous() }
        is PlayerAction.SeekBackward -> player.seekBackward()
        is PlayerAction.SeekForward -> player.seekForward()
        is PlayerAction.TogglePlay -> player.toggleIsPlaying()
        is PlayerAction.ToggleAudioOnlyMode -> player.toggleAudioOnlyMode()
    }
    
    fun switchPlaybackQuality(videoOption: VideoOption) = 
        player.switchPlaybackQuality(videoOption)

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
        val currentVideo = currentVideoData.value ?: return

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
        val currentVideo = currentVideoData.value ?: return

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

    fun subscribe(channel: ChannelItem.OnlineChannelItem) {
        viewModelScope.launch {
            channelRepo.subscribe(channel).collect()
        }
    }
}
