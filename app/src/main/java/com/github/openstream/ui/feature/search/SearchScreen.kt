package com.github.openstream.ui.feature.search

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.designsystem.dataitem.DataItemList
import com.github.openstream.ui.feature.search.components.SearchField
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun SearchScreen(
    topBar: (@Composable () -> Unit) -> Unit,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (DataItem.Playlist) -> Unit,
    playVideo: (String) -> Unit,
) {
    val viewModel = koinViewModel<SearchViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery
    val searchFieldInteractionSource = remember { MutableInteractionSource() }
    val isSearchFieldFocused by searchFieldInteractionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val items = viewModel.items

    topBar {
        SearchField(
            searchQuery = searchQuery,
            focusManager = focusManager,
            setSearchQuery = { viewModel.searchQuery.value = it },
            isSearchFieldFocused = isSearchFieldFocused,
            searchFieldInteractionSource = searchFieldInteractionSource,
            search = { viewModel.search() },
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
        when (uiState) {
            is SearchViewModel.UiState.Empty -> {}
            is SearchViewModel.UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            is SearchViewModel.UiState.Error -> Text(
                text = (uiState as SearchViewModel.UiState.Error).message ?: "",
                modifier = Modifier.align(Alignment.Center)
            )

            is SearchViewModel.UiState.Success -> DataItemList(
                items = items,
                toPlaylistScreen = toPlaylistScreen,
                toChannelScreen = toChannelScreen,
                playVideo = playVideo,
                loadNextPage = { viewModel.getNextPage() },
                addToWatchLater = { viewModel.addToWatchLater(it) },
            )
        }
    }
}
