@file:Suppress("UNCHECKED_CAST")

package com.github.openstream.ui.designsystem.components.dataitem

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.github.openstream.core.shared.MiniPlayerConfig
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.ui.navigation.NavigationViewModel
import com.github.openstream.ui.navigation.routes.Tabs
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DataItemList(
    items: SnapshotStateList<DataItem>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListUniqueId: String = "",
    shouldViewChannel: Boolean = true,
    scrollToTopTab: Tabs? = null,
    toChannelScreen: (String) -> Unit = {},
    toPlaylistScreen: (PlaylistItem) -> Unit = {},
    loadNextPage: () -> Unit = {},
    savePlaylist: (PlaylistItem.OnlinePlaylistItem) -> Unit = {},
    addToWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromWatchLater: ((VideoItem) -> Unit)? = null,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit = {},
    unsubscribe: (ChannelItem.OfflineFirstChannelItem) -> Unit = {},
    removeFromPlaylist: ((VideoItem) -> Unit)? = null,
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
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        ) {
            items(
                items,
                key = { it.key + lazyListUniqueId },
                contentType = { it }
            ) {
                DataItem(
                    modifier = Modifier
                        .animateItem(),
                    shouldViewChannel = shouldViewChannel,
                    item = it,
                    toChannelScreen = toChannelScreen,
                    subscribe = subscribe,
                    toPlaylistScreen = toPlaylistScreen,
                    savePlaylist = savePlaylist,
                    removeFromWatchLater = removeFromWatchLater,
                    unsubscribe = unsubscribe,
                    addToWatchLater = addToWatchLater,
                    removeFromPlaylist = removeFromPlaylist,
                )
            }
            item {
                val widthToScreenWidthRatio =
                    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) MiniPlayerConfig.LANDSCAPE_WIDTH_TO_SCREEN_WIDTH_RATIO
                    else MiniPlayerConfig.WIDTH_TO_SCREEN_WIDTH_RATIO
                if (items.isNotEmpty()) Spacer(Modifier.height((screenWidth * widthToScreenWidthRatio * 9 / 16).dp))
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
    loadNextPage: () -> Unit = {},
    savePlaylist: (PlaylistItem.OnlinePlaylistItem) -> Unit = {},
    addToWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromWatchLater: ((VideoItem) -> Unit)? = null,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit = {},
    unsubscribe: (ChannelItem.OfflineFirstChannelItem) -> Unit = {},
    removeFromPlaylist: ((VideoItem) -> Unit)? = null,
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
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
    ) {
        items(
            items,
            key = { it.key + lazyListUniqueId },
            contentType = { it },
        ) {
            DataItem(
                modifier = Modifier
                    .animateItem(),
                shouldViewChannel = shouldViewChannel,
                item = it,
                toChannelScreen = toChannelScreen,
                toPlaylistScreen = toPlaylistScreen,
                savePlaylist = savePlaylist,
                removeFromWatchLater = removeFromWatchLater,
                addToWatchLater = addToWatchLater,
                unsubscribe = unsubscribe,
                subscribe = subscribe,
                removeFromPlaylist = removeFromPlaylist,
            )
        }
        item {
            val widthToScreenWidthRatio =
                if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) MiniPlayerConfig.LANDSCAPE_WIDTH_TO_SCREEN_WIDTH_RATIO
                else MiniPlayerConfig.WIDTH_TO_SCREEN_WIDTH_RATIO
            if (items.isNotEmpty()) Spacer(Modifier.height((screenWidth * widthToScreenWidthRatio * 9 / 16).dp))
        }
    }
}
