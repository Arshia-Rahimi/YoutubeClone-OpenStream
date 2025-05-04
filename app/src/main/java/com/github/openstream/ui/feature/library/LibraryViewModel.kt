package com.github.openstream.ui.feature.library

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.schabi.newpipe.extractor.timeago.patterns.vi

class LibraryViewModel(
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    val playlists = mutableStateListOf<DataItem>()
    private val playlistFlow = playlistRepo.localPlaylists
        .onEach { newPlaylists ->
            playlists.clear()
            playlists.addAll(newPlaylists)
        }.launchIn(viewModelScope)
    
    private val playlistActionChannel = Channel<Pair<String, String>>()
    val playlistActionErrorEvent = playlistActionChannel.receiveAsFlow()

    fun createPlaylist(title: String) {
        viewModelScope.launch {
            playlistRepo.createPlaylist(title).collect {
                when(it) {
                    is Resource.Error -> {
                        delay(1000L)
                        playlistActionChannel.send("failed to create playlist: $title" to it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }
    
    fun deletePlaylist(playlist: DataItem.Playlist) {
        viewModelScope.launch {
            playlistRepo.deletePlaylist(playlist).collect {
                when(it) {
                    is Resource.Error -> {
                        delay(1000L)
                        playlistActionChannel.send("failed to delete playlist: ${playlist.name}" to it.message.toString())
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
                    when(it) {
                        is Resource.Error -> {
                            delay(1000L)
                            playlistActionChannel.send("failed to save playlist: ${playlist.name}" to it.message.toString())
                        }
                        else -> Unit
                    }
                }
        }
    }
}
