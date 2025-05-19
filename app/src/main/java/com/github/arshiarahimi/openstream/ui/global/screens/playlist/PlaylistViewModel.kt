package com.github.arshiarahimi.openstream.ui.global.screens.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.onFirst
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.extractor.OfflineFirstPlaylistExtractor
import com.github.arshiarahimi.openstream.core.model.extractor.OnlinePlaylistExtractor
import com.github.arshiarahimi.openstream.core.model.extractor.PlaylistExtractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlaylistViewModel(
    var playlist: PlaylistItem,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {
    
    var playlistObject: PlaylistExtractor? = null
    
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
                        if (it == null) {
                            // todo nav back
                        } else videos.addAll(it)
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
                            playlistRepo.getPlaylistFirstPage(playlistObject as OfflineFirstPlaylistExtractor)
                                .collect { }
                        }
                        
                        else -> Unit
                    }
                }
            
            _isRefreshing.value = false
        }
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
        
        viewModelScope.launch {
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
                if (it == null) {
                    // todo nav back
                } else videos.addAll(it)
            }.onFirst {
                // if the playlist is offlineFirst and there is no videos saved fetch it
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
    
}
