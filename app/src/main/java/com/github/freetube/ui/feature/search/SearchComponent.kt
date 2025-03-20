package com.github.freetube.ui.feature.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.github.freetube.core.data.YtRepository
import com.github.freetube.core.extractor.DataItem
import com.github.freetube.ui.designsystem.TabComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class SearchComponent(
    componentContext: ComponentContext,
) : TabComponent, KoinComponent, ComponentContext by componentContext {
    private val ytRepo = getKoin().inject<YtRepository>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _searchSuggestion = MutableStateFlow("")
    val searchSuggestion = _searchSuggestion.asStateFlow()
    private val _isCorrectedSearch = MutableStateFlow(false)
    val isCorrectedSearch = _isCorrectedSearch.asStateFlow()

    val results = mutableStateListOf<DataItem>()

    var searchQuery = mutableStateOf("")

//    fun search() {
//        viewModelScope.launch(Dispatchers.IO) {
//            ytRepo.search(searchQuery.value)
//                .collect {
//                    when (it) {
//                        is Resource.Loading -> _isLoading.value = true
//                        is Resource.Error -> {
//                            println(it.message)
//                            _isLoading.value = false
//                        }
//
//                        is Resource.Success -> {
//                            _searchSuggestion.value = it.data.searchSuggestion
//                            _isCorrectedSearch.value = it.data.isCorrectedSearch
//                            results += it.data.firstPage
//                            _isLoading.value = false
//                        }
//                    }
//                }
//        }
//    }
//
//    fun getNextPage() {
//        viewModelScope.launch {
//            ytRepo.getNextPage().collect {
//                when (it) {
//                    is Resource.Error -> {
//                        println(it.message)
//                    }
//
//                    is Resource.Success -> {
//                        it.data?.let { nextPage -> results += nextPage }
//                    }
//
//                    else -> {}
//                }
//            }
//        }
//    }
}
