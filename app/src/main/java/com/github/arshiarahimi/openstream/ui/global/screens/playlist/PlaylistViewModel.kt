package com.github.arshiarahimi.openstream.ui.global.screens.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.onFirst
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.core.model.extractordata.DataItem
import com.github.arshiarahimi.openstream.core.model.extractordata.OfflineFirstPlaylist
import com.github.arshiarahimi.openstream.core.model.extractordata.OnlinePlaylist
import com.github.arshiarahimi.openstream.core.model.extractordata.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.extractordata.YoutubePlaylist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlaylistViewModel(
    var playlist: PlaylistItem,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    var playlistObject: YoutubePlaylist? = null

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    // todo cleanup
    val videos: SnapshotStateList<DataItem> = mutableStateListOf<DataItem>().apply {
        when (playlist) {
            is PlaylistItem.OnlinePlaylistItem -> {
                playlistRepo.getPlaylist(playlist as PlaylistItem.OnlinePlaylistItem)
                    .onEach {
                        when (it) {
                            is Resource.Success -> {
                                playlistObject = it.data
                                getPlaylistFirstPage()
                            }

                            else -> Unit
                        }
                    }.launchIn(viewModelScope)
            }

            is PlaylistItem.LocalPlaylistItem -> {
                playlistRepo.getPlaylistSavedVideos(playlist as PlaylistItem.LocalPlaylistItem)
                    .onEach {
                        videos.clear()
                        videos.addAll(it)
                    }.onFirst {
                        if (videos.isEmpty() && playlist is PlaylistItem.OfflineFirstPlaylistItem) {
                            playlistRepo.getPlaylist(playlist as PlaylistItem.OfflineFirstPlaylistItem)
                                .collect {
                                    when (it) {
                                        is Resource.Success -> {
                                            playlistObject = it.data
                                            playlistRepo.getPlaylistFirstPage(playlistObject as OfflineFirstPlaylist)
                                                .collect {
                                                    when (it) {
                                                        is Resource.Error -> "failed to retrieve playlist items"
                                                        else -> Unit
                                                    }
                                                }
                                        }

                                        else -> Unit
                                    }
                                }
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    private fun getPlaylistFirstPage() {
        viewModelScope.launch {
            playlistRepo.getPlaylistFirstPage(playlistObject as OnlinePlaylist)
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            videos.addAll(it.data)
                        }

                        else -> Unit
                    }
                }
        }
    }

    fun getNextPage() {
        when (playlistObject) {
            null -> Unit
            is OnlinePlaylist -> {
                playlistRepo.getNextPage(playlistObject as OnlinePlaylist)
                    .onEach {
                        when (it) {
                            is Resource.Success ->
                                videos.addAll(it.data)

                            else -> Unit
                        }
                    }.launchIn(viewModelScope)
            }

            is OfflineFirstPlaylist -> {
                playlistRepo.getPlaylistSavedVideos((playlistObject as OfflineFirstPlaylist).data)
                    .onEach {
                        videos.clear()
                        videos.addAll(it)
                    }.launchIn(viewModelScope)
            }
        }
    }

    fun syncPlaylist() {
        if (playlist !is PlaylistItem.OfflineFirstPlaylistItem) return
        viewModelScope.launch {
            _isRefreshing.value = true

            playlistRepo.getPlaylist(playlist as PlaylistItem.YoutubePlaylistItem)
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            playlistObject = it.data
                            playlistRepo.getPlaylistFirstPage(playlistObject as OfflineFirstPlaylist)
                                .collect { }
                        }

                        else -> Unit
                    }
                }

            _isRefreshing.value = false
        }
    }
}
