package com.github.openstream.ui.global.screens.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.R
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.onFirst
import com.github.openstream.core.common.util.sendPulse
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.dataitem.DataItem
import com.github.openstream.core.model.dataitem.PlaylistItem
import com.github.openstream.core.model.dataitem.VideoItem
import com.github.openstream.core.model.extractor.OfflineFirstPlaylistExtractor
import com.github.openstream.core.model.extractor.OnlinePlaylistExtractor
import com.github.openstream.core.model.extractor.PlaylistExtractor
import com.github.openstream.core.shared.DefaultPlaylists
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class PlaylistViewModel(
    playlist: PlaylistItem,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    val playlist = if (playlist !is PlaylistItem.LocalPlaylistItem) MutableStateFlow(playlist)
    else playlistRepo.getPlaylistItem(playlist.id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), playlist)

    val videos: SnapshotStateList<DataItem> = mutableStateListOf<DataItem>()
        .apply {
            when (playlist) {
                is PlaylistItem.OnlinePlaylistItem -> getOnlinePlaylistInitialData()
                is PlaylistItem.LocalPlaylistItem -> getLocalPlaylistInitialData()
            }
        }

    var playlistObject: PlaylistExtractor? = null
    private val _navBack = Channel<Unit>()
    val navBack = _navBack.receiveAsFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getNextPage() {
        when (playlistObject) {
            null -> Unit
            is OnlinePlaylistExtractor -> {
                playlistRepo.getNextPage(playlistObject as OnlinePlaylistExtractor)
                    .onEach {
                        when (it) {
                            is Resource.Success ->
                                videos.addAll(it.data)

                            else -> Unit
                        }
                    }.launchIn(viewModelScope)
            }

            is OfflineFirstPlaylistExtractor -> {
                playlistRepo.getPlaylistSavedVideos((playlistObject as OfflineFirstPlaylistExtractor).data)
                    .onEach { newVideos ->
                        videos.clear()
                        newVideos?.let { videos.addAll(it) } ?: _navBack.sendPulse()
                    }.launchIn(viewModelScope)
            }
        }
    }

    fun syncPlaylist() {
        if (playlist.value !is PlaylistItem.OfflineFirstPlaylistItem) return
        playlistRepo.getPlaylist(playlist.value as PlaylistItem.YoutubePlaylistItem)
            .onStart { _isRefreshing.value = true }
            .onEach {
                when (it) {
                    is Resource.Success -> {
                        playlistObject = it.data
                        playlistRepo.getPlaylistFirstPage(playlistObject as OfflineFirstPlaylistExtractor)
                            .collect { }
                    }

                    else -> Unit
                }
            }
            .onCompletion { _isRefreshing.value = false }
            .launchIn(viewModelScope)
    }

    private fun getOnlinePlaylistInitialData() {
        suspend fun getFirstPage() =
            playlistRepo.getPlaylistFirstPage(playlistObject as OnlinePlaylistExtractor)
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            videos.addAll(it.data)
                        }

                        else -> Unit
                    }
                }

        playlistRepo.getPlaylist(playlist.value as PlaylistItem.OnlinePlaylistItem)
            .onEach {
                when (it) {
                    is Resource.Success -> {
                        playlistObject = it.data
                        getFirstPage()
                    }

                    else -> Unit
                }
            }.launchIn(viewModelScope)

    }

    private fun getLocalPlaylistInitialData() {
        suspend fun getFirstPage() =
            playlistRepo.getPlaylistFirstPage(playlistObject as OfflineFirstPlaylistExtractor)
                .collect {
                    when (it) {
                        is Resource.Error -> "failed to retrieve playlist items"
                        else -> Unit
                    }
                }

        playlistRepo.getPlaylistSavedVideos(playlist.value as PlaylistItem.LocalPlaylistItem)
            .onEach { newVideos ->
                videos.clear()
                newVideos?.let { videos.addAll(it) } ?: _navBack.sendPulse()
            }.onFirst {
                // if is offlineFirst and there is no videos saved fetch it
                if (videos.isEmpty() && playlist.value is PlaylistItem.OfflineFirstPlaylistItem) {
                    playlistRepo.getPlaylist(playlist.value as PlaylistItem.OfflineFirstPlaylistItem)
                        .collect {
                            when (it) {
                                is Resource.Success -> {
                                    playlistObject = it.data
                                    getFirstPage()
                                }

                                else -> Unit
                            }
                        }
                }
            }.launchIn(viewModelScope)
    }

    fun addToWatchLater(video: VideoItem) {
        playlistRepo.addToPlaylist(listOf(video), DefaultPlaylists.WATCH_LATER_ID)
            .onEach {
                when (it) {
                    is Resource.Success -> {
                        SnackBarController.sendEvent(R.string.added_to_watch_later)
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }

    fun removeFromPlaylist(videoItem: VideoItem) {
        if (playlist.value !is PlaylistItem.LocalOnlyPlaylistItem) return

        playlistRepo.removeFromPlaylist(
            listOf(videoItem),
            (playlist.value as PlaylistItem.LocalOnlyPlaylistItem).id
        ).launchIn(viewModelScope)
    }

}
