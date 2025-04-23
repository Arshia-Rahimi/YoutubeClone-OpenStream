package com.github.openstream.ui.feature.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.model.extractordata.SearchResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepo: SearchRepository,
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

    var searchQuery = mutableStateOf("")
    
    fun onAction(action: SearchAction) = when (action) {
        is SearchAction.OnSearch -> search()
        is SearchAction.OnNextPage -> getNextPage()
    }
    
    private fun search() {
        viewModelScope.launch {
            searchRepo.search(searchQuery.value)
                .collect {
                    _uiState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> UiState.Error(it.message)
                        is Resource.Success -> UiState.Success(it.data)
                    }
                }
        }
    }
    
    private fun getNextPage() {
        viewModelScope.launch {
            if (_uiState.value !is UiState.Success) return@launch
            (_uiState.value as UiState.Success).searchResult.let {
                searchRepo.getNextPage(it).collect {
                    // todo
                }
            }
        }
    }
}
