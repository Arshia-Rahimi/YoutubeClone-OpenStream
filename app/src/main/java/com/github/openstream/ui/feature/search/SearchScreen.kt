package com.github.openstream.ui.feature.search

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.openstream.ui.designsystem.components.DataItemList
import com.github.openstream.ui.designsystem.components.LoadingBox
import com.github.openstream.ui.feature.search.components.SearchField
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun SearchScreen(
    topBar: (@Composable () -> Unit) -> Unit,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
) {
    val viewModel = koinViewModel<SearchViewModel>()
    val trigger: (SearchAction) -> Unit = { viewModel.onAction(it) }
    val searchQuery by viewModel.searchQuery
    val results = viewModel.results
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isCorrectedSearch by viewModel.isCorrectedSearch.collectAsStateWithLifecycle()
    val searchSuggestion by viewModel.searchSuggestion.collectAsStateWithLifecycle()
    val searchFieldInteractionSource = remember { MutableInteractionSource() }
    val isSearchFieldFocused by searchFieldInteractionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    
    topBar {
        SearchField(
            searchQuery = searchQuery,
            focusManager = focusManager,
            setSearchQuery = { viewModel.searchQuery.value = it },
            isSearchFieldFocused = isSearchFieldFocused,
            searchFieldInteractionSource = searchFieldInteractionSource,
            isCorrectedSearch = isCorrectedSearch,
            searchSuggestion = searchSuggestion,
            search = { trigger(SearchAction.OnSearch) },
            focusRequester = focusRequester,
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
        DataItemList(
            items = results,
            toPlaylistScreen = toPlaylistScreen,
            toChannelScreen = toChannelScreen,
            playVideo = playVideo,
            loadNextPage = { trigger(SearchAction.OnNextPage) },
        ) 
        if (isLoading) LoadingBox()
    }
}
