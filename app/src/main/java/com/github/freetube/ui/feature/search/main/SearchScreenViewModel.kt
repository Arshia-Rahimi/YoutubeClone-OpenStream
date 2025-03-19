package com.github.freetube.ui.feature.search.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.YtRepository
import com.github.freetube.core.extractor.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(SavedStateHandleSaveableApi::class)
class SearchScreenViewModel(
    private val ytRepo: YtRepository,
    private val handle: SavedStateHandle,
): ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _searchSuggestion = MutableStateFlow("")
    val searchSuggestion = _searchSuggestion.asStateFlow()
    private val _isCorrectedSearch = MutableStateFlow(false)
    val isCorrectedSearch = _isCorrectedSearch.asStateFlow()

    val results by handle.saveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { mutableStateListOf(*it.toTypedArray()) }
        ),
        init = { mutableStateListOf<DataItem>() }
    )
    val searchQuery by handle.saveable(
        saver = Saver(
            save = { it.value.text },
            restore = { mutableStateOf(TextFieldValue(it)) }
        ),
        init = { mutableStateOf(TextFieldValue()) }
    )
   
    fun setSearchQuery(value: TextFieldValue) {
        searchQuery.value = value
    }
    
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
                    is Resource.Error -> {
                        println(it.message)
                    }
                    is Resource.Success -> {
                        it.data?.let { nextPage -> results += nextPage }
                    }
                    else -> {}
                }
            }
        }
    }
}
