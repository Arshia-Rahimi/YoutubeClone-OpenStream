package com.github.openstream.ui.global.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.playlist.PlaylistMetadata
import com.github.openstream.core.extractor.playlist.PlaylistUnit
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

    private lateinit var playlist: PlaylistUnit
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
                            items += it.data.firstPage.items
                            UiState.Success(it.data.firstPage)
                        }
                    }
                }
        }
    }


    fun getNextPage() {
        viewModelScope.launch {
            if (playlist.nextPage == null) return@launch
            playlistRepository.getNextPage(playlist)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Error -> {}
                        is Resource.Success -> {
                            it.data?.let { items += it }
                        }
                    }
                }
        }
    }
}
