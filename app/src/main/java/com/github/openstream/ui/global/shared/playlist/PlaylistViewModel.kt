package com.github.openstream.ui.global.shared.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.LocalPlaylist
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.YoutubePlaylist
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
    
    init {
        viewModelScope.launch {
            playlistRepository.getPlaylist(this@PlaylistViewModel.playlistDataItem)
                .collect {
                    _uiState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> UiState.Error(it.message)
                        is Resource.Success -> {
                            UiState.Success(it.data)
                        }
                    }
                }
        }
    }
    
    
    fun getNextPage() {
        viewModelScope.launch {
            if (uiState.value !is UiState.Success) return@launch
            if ((uiState.value as UiState.Success).playlist is LocalPlaylist) return@launch
            playlistRepository.getNextPage((uiState.value as UiState.Success).playlist as YoutubePlaylist)
                .collect {
                    // todo not sure i need to do anything here
                }
        }
    }
}
