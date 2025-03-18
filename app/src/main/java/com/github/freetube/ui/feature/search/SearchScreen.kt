package com.github.freetube.ui.feature.search

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.ui.designsystem.DataItem
import com.github.freetube.ui.designsystem.LoadingBox
import com.github.freetube.ui.feature.search.components.SearchField
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen() {
    val viewModel = koinViewModel<SearchScreenViewModel>()
    var searchQuery by viewModel.searchQuery
    val results = viewModel.results
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLoadingNextPage by viewModel.isLoadingNextPage.collectAsStateWithLifecycle()
    val isCorrectedSearch by viewModel.isCorrectedSearch.collectAsStateWithLifecycle()
    val searchSuggestion by viewModel.searchSuggestion.collectAsStateWithLifecycle()
    val searchFieldInteractionSource = remember { MutableInteractionSource() }
    val isSearchFieldFocused by searchFieldInteractionSource.collectIsFocusedAsState()
    val lazyColumnState = rememberLazyListState()
    val shouldLoadNextPage by remember { derivedStateOf { !lazyColumnState.canScrollForward } }
    
    LaunchedEffect(shouldLoadNextPage) {
        if(shouldLoadNextPage) viewModel.getNextPage()
    }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchField(
            searchQuery = searchQuery,
            setSearchQuery = { searchQuery = it },
            isSearchFieldFocused = isSearchFieldFocused,
            searchFieldInteractionSource = searchFieldInteractionSource,
            search = { viewModel.search() }
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = lazyColumnState,
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterVertically,
            )
        ) {
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
                if (isLoadingNextPage) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
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
