package com.github.openstream.ui.global.shared.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import com.github.openstream.core.model.playlist.Playlist
import com.github.openstream.core.model.playlist.YoutubePlaylist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val url: String,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val playlistMetadata: PlaylistMetadata) : UiState
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private lateinit var playlist: Playlist
    val items = mutableStateListOf<DataItem>()

    init {
        viewModelScope.launch {
            playlistRepository.getPlaylist(url)
                .collect {
                    _uiState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> UiState.Error(it.message)
                        is Resource.Success -> {
                            playlist = it.data
                            items += it.data.items
                            UiState.Success(it.data.metadata)
                        }
                    }
                }
        }
    }


    fun getNextPage() {
        viewModelScope.launch {
            if (playlist !is YoutubePlaylist) return@launch
            playlistRepository.getNextPage(playlist as YoutubePlaylist)
                .collect {
                    // todo not sure i need to do anything here
                }
        }
    }
}
