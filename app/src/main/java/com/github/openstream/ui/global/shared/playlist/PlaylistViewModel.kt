package com.github.openstream.ui.global.shared.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danrusu.pods4k.immutableArrays.toList
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    val items = mutableStateListOf<DataItem>()

    init {
        viewModelScope.launch {
            playlistRepository.getPlaylist(this@PlaylistViewModel.playlistDataItem)
                .collect {
                    _uiState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> UiState.Error(it.message)
                        is Resource.Success -> {
                            items.addAll(it.data.items.toList())
                            UiState.Success(it.data)
                        }
                    }
                }
        }
    }

    fun getNextPage() {
        viewModelScope.launch {
            if (_uiState.value !is UiState.Success) return@launch
            playlistRepository.getNextPage(_uiState.value as Playlist)
                .collect {
                    if (it is Resource.Success) {
                        // todo sort and add items
                    }
                }
        }
    }
}
