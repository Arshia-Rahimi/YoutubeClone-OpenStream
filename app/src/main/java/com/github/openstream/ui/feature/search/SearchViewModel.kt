package com.github.openstream.ui.feature.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.R
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.SearchResult
import com.github.openstream.core.shared.WATCH_LATER_ID
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepo: SearchRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {
    
    companion object {
        val searchFieldFocusEvent = Channel<Unit>()
        val scrollToTopEvent = Channel<Unit>()
    }
    
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
    
    fun addToWatchLater(video: DataItem.Video) {
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
}
