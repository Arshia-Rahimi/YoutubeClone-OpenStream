package com.github.arshiarahimi.openstream.ui.global.screens.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.onFirst
import com.github.arshiarahimi.openstream.core.common.util.sendPulse
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.model.extractor.OfflineFirstPlaylistExtractor
import com.github.arshiarahimi.openstream.core.model.extractor.OnlinePlaylistExtractor
import com.github.arshiarahimi.openstream.core.model.extractor.PlaylistExtractor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow

class PlaylistViewModel(
    var playlist: PlaylistItem,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    var playlistObject: PlaylistExtractor? = null
    private val _navBack = Channel<Unit>()
    val navBack = _navBack.receiveAsFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    val videos: SnapshotStateList<DataItem> = mutableStateListOf<DataItem>()
        .apply {
            when (playlist) {
                is PlaylistItem.OnlinePlaylistItem -> getOnlinePlaylistInitialData()
                is PlaylistItem.LocalPlaylistItem -> getLocalPlaylistInitialData()
            }
        }

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
                    .onEach {
                        videos.clear()
                        if (it == null) _navBack.sendPulse()
                        else videos.addAll(it)
                    }.launchIn(viewModelScope)
            }
        }
    }

    fun syncPlaylist() {
        if (playlist !is PlaylistItem.OfflineFirstPlaylistItem) return
        playlistRepo.getPlaylist(playlist as PlaylistItem.YoutubePlaylistItem)
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

        playlistRepo.getPlaylist(playlist as PlaylistItem.OnlinePlaylistItem)
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

        playlistRepo.getPlaylistSavedVideos(playlist as PlaylistItem.LocalPlaylistItem)
            .onEach {
                videos.clear()
                if (it == null) _navBack.sendPulse()
                else videos.addAll(it)
            }.onFirst {
                // if is offlineFirst and there is no videos saved fetch it
                if (videos.isEmpty() && playlist is PlaylistItem.OfflineFirstPlaylistItem) {
                    playlistRepo.getPlaylist(playlist as PlaylistItem.OfflineFirstPlaylistItem)
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

    fun removeFromPlaylist(videoItem: VideoItem) {
        if (playlist !is PlaylistItem.LocalOnlyPlaylistItem) return

        playlistRepo.removeFromPlaylist(
            listOf(videoItem),
            (playlist as PlaylistItem.LocalOnlyPlaylistItem).id
        ).launchIn(viewModelScope)
    }

}
