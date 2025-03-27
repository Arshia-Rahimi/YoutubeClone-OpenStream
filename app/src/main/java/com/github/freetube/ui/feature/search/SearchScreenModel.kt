package com.github.freetube.ui.feature.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.SearchRepository
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.search.SearchUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchScreenModel(
    private val ytRepo: SearchRepository,
) : ScreenModel {

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
        screenModelScope.launch {
            ytRepo.search(searchQuery.value)
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
        screenModelScope.launch {
            if (search.nextPage == null) return@launch
            ytRepo.getNextPage(search).collect {
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
