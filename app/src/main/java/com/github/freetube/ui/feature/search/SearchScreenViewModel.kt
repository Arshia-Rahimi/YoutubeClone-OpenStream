package com.github.freetube.ui.feature.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.YtRepository
import com.github.freetube.core.extractor.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val ytRepo: YtRepository,
): ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage = _isLoadingNextPage.asStateFlow()
    private val _searchSuggestion = MutableStateFlow("")
    val searchSuggestion = _searchSuggestion.asStateFlow()
    private val _isCorrectedSearch = MutableStateFlow(false)
    val isCorrectedSearch = _isCorrectedSearch.asStateFlow()
    
    val results = mutableStateListOf<DataItem>()
    val searchQuery = mutableStateOf(TextFieldValue())
    
    fun search() {
        viewModelScope.launch(Dispatchers.IO) {
            ytRepo.search(searchQuery.value.text)
                .collect { 
                    when(it) {
                        is Resource.Loading -> _isLoading.value = true
                        is Resource.Error -> {
                            println(it.message)
                            _isLoading.value = false
                        }
                        is Resource.Success -> {
                            _searchSuggestion.value = it.data.searchSuggestion
                            _isCorrectedSearch.value = it.data.isCorrectedSearch
                            results += it.data.firstPage
                            _isLoading.value = false
                        }
                    }
                }
        }
    }
    
    fun getNextPage() {
        viewModelScope.launch {
            ytRepo.getNextPage().collect {
                when(it) {
                    is Resource.Loading -> _isLoadingNextPage.value = true
                    is Resource.Error -> {
                        println(it.message)
                        _isLoadingNextPage.value = false
                    }
                    is Resource.Success -> {
                        it.data?.let { nextPage -> results += nextPage }
                        _isLoadingNextPage.value = false
                    }
                }
            }
        }
    }
}
