package com.github.openstream.ui.global.shared.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danrusu.pods4k.immutableArrays.toList
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistDataItem: DataItem.Playlist,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val playlist: Playlist) : UiState
    }

    val items = mutableStateListOf<DataItem>()
    val uiState = playlistRepository.getPlaylist(playlistDataItem)
        .map {
            when (it) {
                is Resource.Loading -> UiState.Loading
                is Resource.Error -> UiState.Error(it.message)
                is Resource.Success -> {
                    items.addAll(it.data.items.toList())
                    UiState.Success(it.data)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading,
        )

    fun syncPlaylist() {
        viewModelScope.launch {
            if (uiState.value !is UiState.Success) return@launch
            playlistRepository.syncPlaylist((uiState.value as UiState.Success).playlist)
                .collect {
                    Snapshot.withMutableSnapshot {
                        // todo sort and add items
                    }
                }
        }
    }

    fun getNextPage() {
        viewModelScope.launch {
            if (uiState.value !is UiState.Success) return@launch
            playlistRepository.getNextPage((uiState.value as UiState.Success).playlist)
                .collect {
                    if (it is Resource.Success) {
                        // todo sort and add items
                        Snapshot.withMutableSnapshot {
                            items
                        }
                    }
                }
        }
    }
}
