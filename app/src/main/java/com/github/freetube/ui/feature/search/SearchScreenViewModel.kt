package com.github.freetube.ui.feature.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.freetube.core.data.YtRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val ytRepo: YtRepository,
): ViewModel() {
    
    val searchQuery = mutableStateOf("")
    
    fun search() {
        viewModelScope.launch(Dispatchers.IO) {
            ytRepo.search(searchQuery.value).collect(::println)
        }
    }
    
    fun getNextPage() {
        viewModelScope.launch {
            ytRepo.getNextPage().collect(::println)
        }
    }
}
