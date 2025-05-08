package com.github.openstream.ui.designsystem.dataitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.feature.search.SearchViewModel
import com.github.openstream.ui.global.player.MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DataItemList(
    items: SnapshotStateList<DataItem>,
    shouldViewChannel: Boolean = true,
    onRefresh: () -> Unit = {},
    isRefreshing: Boolean,
    toChannelScreen: (String) -> Unit = {},
    toPlaylistScreen: (DataItem.Playlist) -> Unit = {},
    playVideo: (String) -> Unit = {},
    loadNextPage: () -> Unit = {},
    savePlaylist: (DataItem.Playlist) -> Unit = {},
    deletePlaylist: (DataItem.Playlist) -> Unit = {},
    addToWatchLater: ((DataItem.Video) -> Unit)? = null,
    removeFromWatchLater: ((DataItem.Video) -> Unit)? = null,
) {
    val lazyColumnState = rememberLazyListState()
    val shouldLoadNextPage by remember {
        derivedStateOf { !lazyColumnState.canScrollForward && items.isNotEmpty() }
    }
    val config = LocalConfiguration.current
    // todo check the error
    val screenWidth = config.screenWidthDp

    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) loadNextPage()
    }
    LaunchedEffect(Unit) {
        SearchViewModel.scrollToTopEvent
            .receiveAsFlow()
            .collect {
                lazyColumnState.animateScrollToItem(0)
            }
    }

    // todo top bar is not shown 
    PullToRefreshBox(
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
    ) {
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
        ) {
            items(
                items,
                key = { it.key },
                contentType = { it }
            ) {
                DataItem(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 8.dp)
                        .animateItem(),
                    shouldViewChannel = shouldViewChannel,
                    item = it,
                    toChannelScreen = toChannelScreen,
                    toPlaylistScreen = toPlaylistScreen,
                    playVideo = playVideo,
                    savePlaylist = savePlaylist,
                    deletePlaylist = deletePlaylist,
                    removeFromWatchLater = removeFromWatchLater,
                    addToWatchLater = addToWatchLater,
                )
            }
            item {
                if (items.isNotEmpty()) Spacer(Modifier.height((screenWidth * MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO * 9 / 16).dp))
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DataItemList(
    items: SnapshotStateList<DataItem>,
    shouldViewChannel: Boolean = true,
    toChannelScreen: (String) -> Unit = {},
    toPlaylistScreen: (DataItem.Playlist) -> Unit = {},
    playVideo: (String) -> Unit = {},
    loadNextPage: () -> Unit = {},
    savePlaylist: (DataItem.Playlist) -> Unit = {},
    deletePlaylist: (DataItem.Playlist) -> Unit = {},
    addToWatchLater: ((DataItem.Video) -> Unit)? = null,
    removeFromWatchLater: ((DataItem.Video) -> Unit)? = null,
) {
    val lazyColumnState = rememberLazyListState()
    val shouldLoadNextPage by remember {
        derivedStateOf { !lazyColumnState.canScrollForward && items.isNotEmpty() }
    }
    val config = LocalConfiguration.current
    // todo check the error
    val screenWidth = config.screenWidthDp
    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) loadNextPage()
    }
    LaunchedEffect(Unit) {
        SearchViewModel.scrollToTopEvent
            .receiveAsFlow()
            .collect {
                lazyColumnState.animateScrollToItem(0)
            }
    }

    LazyColumn(
        state = lazyColumnState,
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
    ) {
        items(
            items,
            key = { it.key },
            contentType = { it }
        ) {
            DataItem(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
                    .animateItem(),
                shouldViewChannel = shouldViewChannel,
                item = it,
                toChannelScreen = toChannelScreen,
                toPlaylistScreen = toPlaylistScreen,
                playVideo = playVideo,
                savePlaylist = savePlaylist,
                deletePlaylist = deletePlaylist,
                removeFromWatchLater = removeFromWatchLater,
                addToWatchLater = addToWatchLater,
            )
        }
        item {
            if (items.isNotEmpty()) Spacer(Modifier.height((screenWidth * MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO * 9 / 16).dp))
        }
    }
}
