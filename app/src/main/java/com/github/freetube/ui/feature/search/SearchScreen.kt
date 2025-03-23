package com.github.freetube.ui.feature.search

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.ui.designsystem.LoadingBox
import com.github.freetube.ui.designsystem.dataitem.DataItem
import com.github.freetube.ui.feature.search.components.SearchField
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun SearchScreen(
    screenModel: SearchScreenModel,
    topBar: (@Composable () -> Unit) -> Unit,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
) {
    val trigger: (SearchAction) -> Unit = { screenModel.onAction(it) }
    val searchQuery by screenModel.searchQuery
    val results = screenModel.results
    val isLoading by screenModel.isLoading.collectAsStateWithLifecycle()
    val isCorrectedSearch by screenModel.isCorrectedSearch.collectAsStateWithLifecycle()
    val searchSuggestion by screenModel.searchSuggestion.collectAsStateWithLifecycle()
//    var isLoadingNextPage by remember { mutableStateOf(false) }
    val searchFieldInteractionSource = remember { MutableInteractionSource() }
    val isSearchFieldFocused by searchFieldInteractionSource.collectIsFocusedAsState()
    val lazyColumnState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val shouldLoadNextPage by remember {
        derivedStateOf { !lazyColumnState.canScrollForward && results.isNotEmpty() }
    }

    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) trigger(SearchAction.OnNextPage)
//        if (shouldLoadNextPage) {
//            isLoadingNextPage = true
//            trigger(SearchAction.OnNextPage)
//        } else isLoadingNextPage = false
    }

    topBar {
        SearchField(
            searchQuery = searchQuery,
            focusManager = focusManager,
            setSearchQuery = { screenModel.searchQuery.value = it },
            isSearchFieldFocused = isSearchFieldFocused,
            searchFieldInteractionSource = searchFieldInteractionSource,
            isCorrectedSearch = isCorrectedSearch,
            searchSuggestion = searchSuggestion,
            search = { trigger(SearchAction.OnSearch) },
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        focusManager.clearFocus()
                    },
                )
            },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            state = lazyColumnState,
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterVertically,
            )
        ) {
            items(results, key = { it.url + "-" + Uuid.random() }, contentType = { it }) {
                DataItem(
                    item = it,
                    toChannelScreen = toChannelScreen,
                    toPlaylistScreen = toPlaylistScreen,
                    playVideo = {},
                )
            }
            item {
                if (results.isNotEmpty()) Spacer(Modifier.height(48.dp))
            }
//            item {
//                if(isLoadingNextPage) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center,
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                }
//            }
        }
        // todo the box should not be covering the textField
        if (isLoading) LoadingBox()
    }
}
