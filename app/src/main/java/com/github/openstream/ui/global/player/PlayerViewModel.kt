package com.github.openstream.ui.global.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.media3.OpenStreamMediaPlayer.FetchingState
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
    private val videoRepo: VideoRepository,
) : ViewModel() {

    val playerInstance = player.player
    
    val tempVideoName = MutableStateFlow<String?>(null)

    val fetchingState = player.fetchingState
    val currentQuality = player.currentQuality
    val isPlaying = player.isPlaying
    val isBuffering = player.isBuffering
    val isAudioOnlyModeEnabled = player.isAudioOnlyModeEnabled
    
    val currentPosition = player.playerPosition
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)
    val bufferedPosition = player.bufferedPosition
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)
    val playbackSpeed = player.playbackSpeed
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1f)
   

    private val _showMiniPlayer = MutableStateFlow(false)
    val showMiniPlayer = _showMiniPlayer.asStateFlow()

    private val _sheetState = MutableStateFlow(PlayerSheetState.MINI_PLAYER)
    val sheetState = _sheetState.asStateFlow()
    
    private val _isInLandscape = MutableStateFlow(false)
    val isInLandscape = _isInLandscape.asStateFlow()

    val isInitialSnapDone = MutableStateFlow(false)

    val shouldShowFullscreenPlayer =
        combine(
            showMiniPlayer,
            sheetState,
            isInLandscape,
        ) { showMiniPlayer, sheetState, isInLandscape ->
            showMiniPlayer && (sheetState == PlayerSheetState.EXPANDED) && isInLandscape
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val videoLocalState = fetchingState.flatMapLatest {
        if (it !is FetchingState.Success) flow { emit(VideoLocalState()) }
        else {
            val videoId = videoRepo.getVideoId(it.video.url)
            combine(
                videoId?.let { id -> playlistRepo.isInPlaylist(id, DefaultPlaylists.WATCH_LATER_ID) } ?: flow { emit(false) },
                videoId?.let { id -> playlistRepo.isInPlaylist(id, DefaultPlaylists.LIKED_VIDEOS_ID) } ?: flow { emit(false) },
                channelRepo.getChannelId(it.video.channelUrl),
            ) { isInWatchLater, isLiked, channelId ->
                VideoLocalState(isInWatchLater, isLiked, channelId)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VideoLocalState())

    fun processAction(action: PlayerAction) = when (action) {
        is PlayerAction.Start -> start(action.videoItem)
        is PlayerAction.SeekTo -> player.seekTo(action.ms)
        is PlayerAction.SeekBackward -> player.seekBackward()
        is PlayerAction.SeekForward -> player.seekForward()
        is PlayerAction.ToggleAudioOnlyMode -> player.toggleAudioOnlyMode()
        is PlayerAction.Retry -> player.retry()
        is PlayerAction.Pause -> player.pause()
        is PlayerAction.Resume -> player.resume()
        is PlayerAction.SetPlaybackSpeed -> player.setPlaybackSpeed(action.speed.speed)
    }

    fun updateSheetState(sheetState: PlayerSheetState) {
        _sheetState.value = sheetState
    }
    
    fun onOrientationChanged(orientation: Int) {
        _isInLandscape.value =
            orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    }

    fun switchPlaybackQuality(videoOption: VideoOption) =
        player.switchPlaybackQuality(videoOption)

    private fun start(videoItem: VideoItem) {
        tempVideoName.value = videoItem.name
        _showMiniPlayer.value = true
        player.start(videoItem)
    }

    fun dispose() {
        player.clear()
        _showMiniPlayer.value = false
    }

    fun toggleVideoWatchLater() {
        val videoData = when (fetchingState.value) {
            is FetchingState.Error -> (fetchingState.value as FetchingState.Success).video
            else -> return
        }

        when (videoLocalState.value.isInWatchLater) {
            true -> playlistRepo.removeFromPlaylist(
                listOf(videoData.toDataItem()),
                DefaultPlaylists.WATCH_LATER_ID,
            )

            false -> playlistRepo.addToPlaylist(
                listOf(videoData.toDataItem()),
                DefaultPlaylists.WATCH_LATER_ID,
            )
        }.launchIn(viewModelScope)
    }

    fun toggleVideoLiked() {
        val videoData = when (fetchingState.value) {
            is FetchingState.Error -> (fetchingState.value as FetchingState.Success).video
            else -> return
        }

        when (videoLocalState.value.isLiked) {
            true -> playlistRepo.removeFromPlaylist(
                listOf(videoData.toDataItem()),
                DefaultPlaylists.LIKED_VIDEOS_ID,
            )

            false -> playlistRepo.addToPlaylist(
                listOf(videoData.toDataItem()),
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
