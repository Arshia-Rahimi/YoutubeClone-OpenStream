package com.github.freetube.ui.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.ui.designsystem.dataitem.DataItem
import com.github.freetube.ui.global.player.MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO
import com.github.freetube.ui.global.player.PlayerViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun DataItemList(
    items: SnapshotStateList<DataItem>,
    shouldViewChannel: Boolean = true,
    toChannelScreen: (String) -> Unit = {},
    toPlaylistScreen: (String) -> Unit = {},
    playVideo: (String) -> Unit = {},
    loadNextPage: () -> Unit = {},
) {
    val lazyColumnState = rememberLazyListState()
    val shouldLoadNextPage by remember {
        derivedStateOf { !lazyColumnState.canScrollForward && items.isNotEmpty() }
    }
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) loadNextPage()
    }

    LazyColumn(
        state = lazyColumnState,
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
    ) {
        items(
            items,
            key = { it.url + "-" + Uuid.random() },
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
            )
        }
        item {
            if (items.isNotEmpty()) Spacer(Modifier.height((screenWidth * MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO * 9 / 16).dp))
        }
    }
}
