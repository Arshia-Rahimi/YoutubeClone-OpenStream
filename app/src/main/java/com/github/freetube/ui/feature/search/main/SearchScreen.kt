package com.github.freetube.ui.feature.search.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.ui.designsystem.DataItem
import com.github.freetube.ui.designsystem.LoadingBox
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen() {
    val viewModel = koinViewModel<SearchScreenViewModel>()
    val searchQuery by viewModel.searchQuery
    val results = viewModel.results
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isCorrectedSearch by viewModel.isCorrectedSearch.collectAsStateWithLifecycle()
    val searchSuggestion by viewModel.searchSuggestion.collectAsStateWithLifecycle()
    val searchFieldInteractionSource = remember { MutableInteractionSource() }
    val isSearchFieldFocused by searchFieldInteractionSource.collectIsFocusedAsState()
    val lazyColumnState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val shouldLoadNextPage by remember { derivedStateOf { !lazyColumnState.canScrollForward } }
    var a by rememberSaveable { mutableStateOf("") }
    
    LaunchedEffect(shouldLoadNextPage) {
        if(shouldLoadNextPage) viewModel.getNextPage()
    }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { focusManager.clearFocus() },
                )
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
//        SearchField(
//            searchQuery = searchQuery,
//            focusManager = focusManager,
//            setSearchQuery = { viewModel.setSearchQuery(it) },
//            isSearchFieldFocused = isSearchFieldFocused,
//            searchFieldInteractionSource = searchFieldInteractionSource,
//            search = { viewModel.search() }
//        )
        TextField(a, { a = it })
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
        }
    }
    // todo the box should not be covering the textField
    if (isLoading) LoadingBox()
}

@Composable
private fun SearchInfo(
    isCorrectedSearch: Boolean,
    searchSuggestion: String,
) {
    // todo 
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//    ) { 
//        
//    }
}
