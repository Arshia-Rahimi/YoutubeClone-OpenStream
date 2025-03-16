package com.github.freetube.ui.feature.search

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.freetube.core.data.YtRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val ytRepo: YtRepository,
): ViewModel() {
    
    val searchQuery = mutableStateOf(TextFieldValue())
    
    fun search() {
        viewModelScope.launch(Dispatchers.IO) {
            ytRepo.search(searchQuery.value.text).collect(::println)
        }
    }
    
    fun getNextPage() {
        viewModelScope.launch {
            ytRepo.getNextPage().collect(::println)
        }
    }
}
