package com.github.freetube.ui.feature.search

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.ui.designsystem.DataItem
import com.github.freetube.ui.designsystem.LoadingBox
import com.github.freetube.ui.feature.search.components.SearchField
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen() {
    val viewModel = koinViewModel<SearchScreenViewModel>()
    val searchFieldInteractionSource = remember { MutableInteractionSource() }
    val isSearchFieldFocused by searchFieldInteractionSource.collectIsFocusedAsState()
    var searchQuery by viewModel.searchQuery
    val results = viewModel.results
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLoadingNextPage by viewModel.isLoadingNextPage.collectAsStateWithLifecycle()
    val isCorrectedSearch by viewModel.isCorrectedSearch.collectAsStateWithLifecycle()
    val searchSuggestion by viewModel.searchSuggestion.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            SearchField(
                searchQuery = searchQuery,
                setSearchQuery = { searchQuery = it },
                isSearchFieldFocused = isSearchFieldFocused,
                searchFieldInteractionSource = searchFieldInteractionSource,
                search = { viewModel.search() }
            )
        }
        item {
            SearchInfo(
                isCorrectedSearch = isCorrectedSearch,
                searchSuggestion = searchSuggestion,
            )
        }
        items(results, key = { it.url }, contentType = { it }) {
            DataItem(it)
        }
        item {
            if(isLoadingNextPage) CircularProgressIndicator()
        }
    }
    if (isLoading) LoadingBox()
}

@Composable
private fun SearchInfo(
    isCorrectedSearch: Boolean,
    searchSuggestion: String,
) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//    ) { 
//        
//    }
}
