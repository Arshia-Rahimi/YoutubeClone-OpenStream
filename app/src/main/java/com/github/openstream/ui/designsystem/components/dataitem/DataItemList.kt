package com.github.openstream.ui.designsystem.components.dataitem

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
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.github.openstream.app.navigation.NavigationViewModel
import com.github.openstream.app.navigation.routes.Tabs
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistItem
import com.github.openstream.core.model.extractordata.VideoItem
import com.github.openstream.ui.global.player.MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DataItemList(
    items: SnapshotStateList<DataItem>,
    shouldViewChannel: Boolean = true,
    isRefreshing: Boolean,
    scrollToTopTab: Tabs? = null,
    onRefresh: () -> Unit = {},
    toChannelScreen: (String) -> Unit = {},
    toPlaylistScreen: (PlaylistItem) -> Unit = {},
    playVideo: (String) -> Unit = {},
    loadNextPage: () -> Unit = {},
    savePlaylist: (PlaylistItem.OnlinePlaylistItem) -> Unit = {},
    addToWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromWatchLater: ((VideoItem) -> Unit)? = null,
    subscribe: (String) -> Unit = {},
) {
    val lazyColumnState = rememberLazyListState()
    val navViewModel = koinViewModel<NavigationViewModel>()
    val shouldLoadNextPage by remember {
        derivedStateOf { !lazyColumnState.canScrollForward && items.isNotEmpty() }
    }
    val screenWidth = LocalWindowInfo.current.containerSize.width
    
    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) loadNextPage()
    }
    
    scrollToTopTab?.let { currentTab ->
        LaunchedEffect(Unit) {
            navViewModel.tabClickAction
                .collect {
                    if (it == currentTab) {
                        lazyColumnState.animateScrollToItem(0)
                    }
                }
        }
    }
    
    PullToRefreshBox(
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        modifier = Modifier.fillMaxWidth(),
    ) {
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier.fillMaxWidth(),
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
                    subscribe = subscribe,
                    toPlaylistScreen = toPlaylistScreen,
                    playVideo = playVideo,
                    savePlaylist = savePlaylist,
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
    scrollToTopTab: Tabs? = null,
    toChannelScreen: (String) -> Unit = {},
    toPlaylistScreen: (PlaylistItem) -> Unit = {},
    playVideo: (String) -> Unit = {},
    loadNextPage: () -> Unit = {},
    savePlaylist: (PlaylistItem.OnlinePlaylistItem) -> Unit = {},
    addToWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromWatchLater: ((VideoItem) -> Unit)? = null,
    subscribe: (String) -> Unit = {},
) {
    val navViewModel = koinViewModel<NavigationViewModel>()
    val lazyColumnState = rememberLazyListState()
    val shouldLoadNextPage by remember {
        derivedStateOf { !lazyColumnState.canScrollForward && items.isNotEmpty() }
    }
    val screenWidth = LocalWindowInfo.current.containerSize.width.dp
    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) loadNextPage()
    }
    
    
    scrollToTopTab?.let { currentTab ->
        LaunchedEffect(Unit) {
            navViewModel.tabClickAction
                .collect {
                    if (it == currentTab) {
                        lazyColumnState.animateScrollToItem(0)
                    }
                }
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
                removeFromWatchLater = removeFromWatchLater,
                addToWatchLater = addToWatchLater,
                subscribe = subscribe,
            )
        }
        item {
            if (items.isNotEmpty()) Spacer(Modifier.height((screenWidth * MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO * 9 / 16)))
        }
    }
}
