package com.github.openstream.ui.global.screens.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.OfflineFirstPlaylist
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.YoutubePlaylist
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(
    playlistDataItem: PlaylistItem,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val playlist: Playlist) : UiState
    }

    val items = mutableStateListOf<DataItem>()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
            
    val uiState = playlistRepository.getPlaylist(playlistDataItem)
        .map {
            when (it) {
                is Resource.Loading -> UiState.Loading
                is Resource.Error -> UiState.Error(it.message)
                is Resource.Success -> {
                    // todo add date to videos and sort based on sort options
                    items.clear()
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

            val playlist = (uiState.value as UiState.Success).playlist
            if (playlist !is OfflineFirstPlaylist) return@launch

            playlistRepository.syncPlaylist(playlist)
                .collect {
                    when (it) {
                        is Resource.Loading -> _isRefreshing.value = true
                        is Resource.Error -> {
                            _isRefreshing.value = false
                            SnackBarController.sendEvent("failed to sync")
                        }

                        is Resource.Success -> {
                            _isRefreshing.value = false
                        }
                    }
                }
        }
    }

    fun getNextPage() {
        viewModelScope.launch {
            if (uiState.value !is UiState.Success) return@launch

            val playlist = (uiState.value as UiState.Success).playlist
            if (playlist !is YoutubePlaylist) return@launch

            playlistRepository.getNextPage(playlist)
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            items.clear()
                            items.addAll(playlist.items)
                        }

                        else -> Unit
                    }
                }
        }
    }
}
