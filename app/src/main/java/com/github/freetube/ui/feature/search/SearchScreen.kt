package com.github.freetube.ui.feature.search

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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.ui.designsystem.components.DataItemList
import com.github.freetube.ui.designsystem.components.LoadingBox
import com.github.freetube.ui.feature.search.components.SearchField
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun SearchScreen(
    screenModel: SearchScreenModel,
    topBar: (@Composable () -> Unit) -> Unit,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
) {
    val trigger: (SearchAction) -> Unit = { screenModel.onAction(it) }
    val searchQuery by screenModel.searchQuery
    val results = screenModel.results
    val isLoading by screenModel.isLoading.collectAsStateWithLifecycle()
    val isCorrectedSearch by screenModel.isCorrectedSearch.collectAsStateWithLifecycle()
    val searchSuggestion by screenModel.searchSuggestion.collectAsStateWithLifecycle()
    val searchFieldInteractionSource = remember { MutableInteractionSource() }
    val isSearchFieldFocused by searchFieldInteractionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

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
