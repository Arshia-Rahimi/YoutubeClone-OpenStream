package com.github.openstream.ui.feature.library

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.next
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.data.impl.PreferencesRepository
import com.github.openstream.core.model.enums.LibrarySortType
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val playlistRepo: PlaylistRepository,
    private val preferencesRepo: PreferencesRepository,
) : ViewModel() {

    val sortType = preferencesRepo.preferences
        .map { it.librarySortType }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LibrarySortType.CREATED_AT_ASC,
        )

    val playlists = mutableStateListOf<DataItem>()
        .apply {
            playlistRepo.playlists
                .combine(sortType) { newPlaylists, sortType ->
                    val sortedPlaylists = when (sortType) {
                        LibrarySortType.CREATED_AT_ASC -> newPlaylists
                        LibrarySortType.CREATED_AT_DESC -> newPlaylists.reversed()
                        LibrarySortType.NAME_ASC -> newPlaylists.sortedBy { it.name }
                        LibrarySortType.NAME_DESC -> newPlaylists.sortedByDescending { it.name }
                        LibrarySortType.SIZE_ASC -> newPlaylists.sortedByDescending { it.count }
                        LibrarySortType.SIZE_DESC -> newPlaylists.sortedBy { it.count }
                    }
                    clear()
                    addAll(sortedPlaylists)
                }.launchIn(viewModelScope)
        }

    fun toggleSortType() {
        viewModelScope.launch {
            preferencesRepo.setLibrarySortType(sortType.value.next())
        }
    }

    fun deletePlaylist(playlist: PlaylistItem.LocalPlaylistItem) {
        viewModelScope.launch {
            playlistRepo.deletePlaylist(playlist)
                .collect {
                    when (it) {
                        is Resource.Error -> SnackBarController
                            .sendEvent("failed to delete playlist: ${playlist.name}")

                        is Resource.Success -> SnackBarController
                            .sendEvent("deleted playlist: ${playlist.name}")

                        else -> Unit
                    }
                }
        }
    }

    fun savePlaylist(playlist: PlaylistItem.OnlinePlaylistItem) {
        viewModelScope.launch {
            playlistRepo.savePlaylist(playlist)
                .collect {
                    when (it) {
                        is Resource.Error -> SnackBarController
                            .sendEvent("failed to save playlist: ${playlist.name}")

                        is Resource.Success -> SnackBarController
                            .sendEvent("saved playlist: ${playlist.name}")

                        else -> Unit
                    }
                }
        }
    }
}
