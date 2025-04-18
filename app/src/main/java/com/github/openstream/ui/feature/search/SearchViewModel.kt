package com.github.openstream.ui.feature.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.search.SearchUnit
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _searchSuggestion = MutableStateFlow("")
    val searchSuggestion = _searchSuggestion.asStateFlow()
    private val _isCorrectedSearch = MutableStateFlow(false)
    val isCorrectedSearch = _isCorrectedSearch.asStateFlow()

    private lateinit var search: SearchUnit
    val results = mutableStateListOf<DataItem>()
    var searchQuery = mutableStateOf("")

    fun onAction(action: SearchAction) = when (action) {
        is SearchAction.OnSearch -> search()
        is SearchAction.OnNextPage -> getNextPage()
    }

    private fun search() {
        viewModelScope.launch {
            searchRepo.search(searchQuery.value)
                .collect {
                    when (it) {
                        is Resource.Loading -> _isLoading.value = true
                        is Resource.Error -> {
                            _isLoading.value = false
                        }

                        is Resource.Success -> {
                            search = it.data
                            _searchSuggestion.value = it.data.firstPage.searchSuggestion
                            _isCorrectedSearch.value = it.data.firstPage.isCorrectedSearch
                            results.clear()
                            results += it.data.firstPage.firstPage
                            _isLoading.value = false
                        }
                    }
                }
        }
    }

    private fun getNextPage() {
        viewModelScope.launch {
            if (search.nextPage == null) return@launch
            searchRepo.getNextPage(search).collect {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        it.data?.let { nextPage -> results += nextPage }
                    }
                }
            }
        }
    }
}
