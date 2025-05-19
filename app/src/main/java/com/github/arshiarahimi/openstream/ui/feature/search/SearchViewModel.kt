package com.github.arshiarahimi.openstream.ui.feature.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.R
import com.github.arshiarahimi.openstream.core.common.compose.SnackBarController
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.core.data.SearchRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.model.extractordata.SearchResult
import com.github.arshiarahimi.openstream.core.shared.WATCH_LATER_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepo: SearchRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Empty : UiState
        data object Loading : UiState
        data class Error(val message: String?) : UiState
        data class Success(val searchResult: SearchResult) : UiState
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val uiState = _uiState.asStateFlow()
    val items = mutableStateListOf<DataItem>()

    var searchQuery = mutableStateOf("")

    fun search() {
        viewModelScope.launch {
            searchRepo.search(searchQuery.value)
                .collect {
                    _uiState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> UiState.Error(it.message)
                        is Resource.Success -> {
                            items.clear()
                            items.addAll(it.data.items)
                            UiState.Success(it.data)
                        }
                    }
                }
        }
    }

    fun getNextPage() {
        viewModelScope.launch {
            if (_uiState.value !is UiState.Success) return@launch
            (_uiState.value as UiState.Success).searchResult.let {
                searchRepo.getNextPage(it).collect {
                    when (it) {
                        is Resource.Success -> items.addAll(it.data)
                        else -> {}
                    }
                }
            }
        }
    }

    fun addToWatchLater(video: VideoItem) {
        viewModelScope.launch {
            playlistRepo.addToPlaylist(listOf(video), WATCH_LATER_ID)
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            SnackBarController.sendEvent(R.string.added_to_watch_later)
                        }

                        else -> {}
                    }
                }
        }
    }

    fun savePlaylist(playlist: PlaylistItem.OnlinePlaylistItem) {
        playlistRepo.savePlaylist(playlist)
            .onEach {
                when (it) {
                    is Resource.Success -> SnackBarController.sendEvent("saved ${playlist.name}")
                    is Resource.Error -> SnackBarController.sendEvent("failed to save playlist ${playlist.name}")
                    is Resource.Loading -> Unit
                }
            }.launchIn(viewModelScope)
    }
}
