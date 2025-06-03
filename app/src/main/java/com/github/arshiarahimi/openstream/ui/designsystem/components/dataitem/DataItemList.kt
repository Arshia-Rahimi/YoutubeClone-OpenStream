package com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.github.arshiarahimi.openstream.app.navigation.NavigationViewModel
import com.github.arshiarahimi.openstream.app.navigation.routes.Tabs
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.shared.MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DataItemList(
    items: SnapshotStateList<DataItem>,
    modifier: Modifier = Modifier,
    lazyListUniqueId: String = "",
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
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit = {},
) {
    val lazyColumnState = rememberLazyListState()
    val navViewModel = koinViewModel<NavigationViewModel>()
    val shouldLoadNextPage by remember {
        derivedStateOf { !lazyColumnState.canScrollForward && items.isNotEmpty() }
    }
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

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
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            items(
                items,
                key = { it.key + lazyListUniqueId },
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

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DataItemList(
    items: SnapshotStateList<DataItem>,
    modifier: Modifier = Modifier,
    lazyListUniqueId: String = "",
    shouldViewChannel: Boolean = true,
    scrollToTopTab: Tabs? = null,
    toChannelScreen: (String) -> Unit = {},
    toPlaylistScreen: (PlaylistItem) -> Unit = {},
    playVideo: (String) -> Unit = {},
    loadNextPage: () -> Unit = {},
    savePlaylist: (PlaylistItem.OnlinePlaylistItem) -> Unit = {},
    addToWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromWatchLater: ((VideoItem) -> Unit)? = null,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit = {},
) {
    val navViewModel = koinViewModel<NavigationViewModel>()
    val lazyColumnState = rememberLazyListState()
    val shouldLoadNextPage by remember {
        derivedStateOf { !lazyColumnState.canScrollForward && items.isNotEmpty() }
    }
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
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
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        items(
            items,
            key = { it.key + lazyListUniqueId },
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
            if (items.isNotEmpty()) Spacer(Modifier.height((screenWidth * MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO * 9 / 16).dp))
        }
    }
}
