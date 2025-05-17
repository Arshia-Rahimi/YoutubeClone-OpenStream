package com.github.openstream.ui.global.screens.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.OfflineFirstPlaylist
import com.github.openstream.core.model.extractordata.OnlinePlaylist
import com.github.openstream.core.model.extractordata.PlaylistItem
import com.github.openstream.core.model.extractordata.YoutubePlaylist
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class PlaylistViewModel(
    var playlist: PlaylistItem,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {
    
    var playlistObject: YoutubePlaylist? = null
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    
    val videos: SnapshotStateList<DataItem> = mutableStateListOf<DataItem>()
        .apply {
            when (playlist) {
                is PlaylistItem.OnlinePlaylistItem -> {
                    playlistRepo.getPlaylist(playlist as PlaylistItem.OnlinePlaylistItem)
                        .onEach {
                            when (it) {
                                is Resource.Success -> {
                                    playlistObject = it.data
                                }
                                
                                else -> Unit
                            }
                        }
                }
                
                is PlaylistItem.LocalOnlyPlaylistItem -> {
                    playlistRepo.getPlaylistSavedVideos(playlist as PlaylistItem.LocalOnlyPlaylistItem)
                        .onEach {
                            videos.clear()
                            videos.addAll(it)
                        }.launchIn(viewModelScope)
                }
                
                is PlaylistItem.OfflineFirstPlaylistItem -> {
                    viewModelScope.launch {
                        supervisorScope {
                            async { syncPlaylist() }
                            // todo get extractor
                            playlistRepo.getPlaylistSavedVideos(playlist as PlaylistItem.OfflineFirstPlaylistItem)
                                .collect {
                                    videos.clear()
                                    videos.addAll(it)
                                }
                        }
                    }
                }
            }
        }
    
    fun syncPlaylist() {
        require(playlistObject is OfflineFirstPlaylist)
        viewModelScope.launch {
        
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
                playlistRepo.getPlaylistSavedVideos(playlistObject as PlaylistItem.LocalPlaylistItem)
                    .onEach {
                        videos.clear()
                        videos.addAll(it)
                    }.launchIn(viewModelScope)
            }
        }
    }
}
