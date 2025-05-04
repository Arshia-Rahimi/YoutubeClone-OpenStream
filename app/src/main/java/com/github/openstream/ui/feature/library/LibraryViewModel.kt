package com.github.openstream.ui.feature.library

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.next
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.data.PreferencesRepository
import com.github.openstream.core.data.imp.PDPreferencesRepository
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.feature.library.components.SortType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val playlistRepo: PlaylistRepository,
    private val preferencesRepo: PreferencesRepository,
) : ViewModel() {
    
    val sortType = preferencesRepo.librarySortType
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SortType.CREATED_AT_ASC)
    
    val playlists = mutableStateListOf<DataItem>()
    private val playlistFlow = playlistRepo.localPlaylists
        .combine(sortType) { newPlaylists, sortType ->
            val sortedPlaylists = when (sortType) {
                SortType.CREATED_AT_ASC -> newPlaylists
                SortType.CREATED_AT_DESC -> newPlaylists.reversed()
                SortType.NAME_ASC -> newPlaylists.sortedBy { it.name }
                SortType.NAME_DESC -> newPlaylists.sortedByDescending { it.name }
                SortType.SIZE_ASC -> newPlaylists.sortedBy { it.count }
                SortType.SIZE_DESC -> newPlaylists.sortedByDescending { it.count }
            }
            
            playlists.clear()
            playlists.addAll(sortedPlaylists)
        }.launchIn(viewModelScope)
    
    private val playlistActionChannel = Channel<Pair<String, String>>()
    val playlistActionErrorEvent = playlistActionChannel.receiveAsFlow()
    
    fun toggleSortType() {
        viewModelScope.launch {
            preferencesRepo.setLibrarySortType(sortType.value.next())
        }
    }
    
    fun createPlaylist(title: String) {
        viewModelScope.launch {
            playlistRepo.createPlaylist(title).collect {
                when (it) {
                    is Resource.Error -> {
                        delay(1000L)
                        playlistActionChannel
                            .send("failed to create playlist: $title" to it.message.toString())
                    }
                    
                    else -> Unit
                }
            }
        }
    }
    
    fun deletePlaylist(playlist: DataItem.Playlist) {
        viewModelScope.launch {
            playlistRepo.deletePlaylist(playlist).collect {
                when (it) {
                    is Resource.Error -> {
                        delay(1000L)
                        playlistActionChannel
                            .send("failed to delete playlist: ${playlist.name}" to it.message.toString())
                    }
                    
                    else -> Unit
                }
            }
        }
    }
    
    fun savePlaylist(playlist: DataItem.Playlist) {
        viewModelScope.launch {
            playlistRepo.savePlaylist(playlist)
                .collect {
                    when (it) {
                        is Resource.Error -> {
                            delay(1000L)
                            playlistActionChannel
                                .send("failed to save playlist: ${playlist.name}" to it.message.toString())
                        }
                        
                        else -> Unit
                    }
                }
        }
    }
    
}
