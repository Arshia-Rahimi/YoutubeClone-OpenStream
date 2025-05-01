package com.github.openstream.ui.feature.library

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    val playlists = mutableStateListOf<DataItem>()
    private val playlistFlow = playlistRepo.localPlaylists
        .onEach { newPlaylists ->
            playlists.clear()
            playlists.addAll(newPlaylists)
        }.launchIn(viewModelScope)

    fun createPlaylist(title: String) {
        viewModelScope.launch {
            playlistRepo.createPlaylist(title)
        }
    }
}
