package com.github.freetube.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.freetube.core.data.YtRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val ytRepo: YtRepository,
): ViewModel() {
    
    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) { 
            ytRepo.search(query)
                .collect(::println)
        }
    }
}
