package com.github.openstream.ui.feature.subscriptions.root

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.openstream.R
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.ui.designsystem.components.dataitem.DataItemList
import com.github.openstream.ui.feature.subscriptions.root.model.SubscriptionsPage
import com.github.openstream.ui.navigation.routes.Tabs
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    toPlaylistScreen: (PlaylistItem) -> Unit,
    toChannelScreen: (String) -> Unit,
) {
    val viewModel = koinViewModel<SubscriptionsViewModel>()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { SubscriptionsPage.entries.size }
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.subs))
                },
            )
        }
    ) { ip ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                SubscriptionsPage.entries.forEach { tab ->
                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .clickable {
                                scope.launch { pagerState.scrollToPage(tab.ordinal) }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(tab.title),
                            color = if (tab.ordinal == pagerState.currentPage) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary,
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(if (tab.ordinal == pagerState.currentPage) MaterialTheme.colorScheme.onSecondary else Color.Transparent),
                        )
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                @Suppress("UNCHECKED_CAST")
                when (page) {
                    SubscriptionsPage.VIDEOS.ordinal ->
                        DataItemList(
                            scrollToTopTab = Tabs.Subscriptions,
                            items = viewModel.videos as SnapshotStateList<DataItem>,
                            toChannelScreen = toChannelScreen,
                            toPlaylistScreen = toPlaylistScreen,
                            isRefreshing = isRefreshing,
                            onRefresh = viewModel::updateSubscriptions,
                            addToWatchLater = viewModel::addToWatchLater,
                        )
                    
                    SubscriptionsPage.CHANNELS.ordinal ->
                        DataItemList(
                            items = viewModel.subscriptions as SnapshotStateList<DataItem>,
                            toChannelScreen = toChannelScreen,
                            scrollToTopTab = Tabs.Subscriptions,
                        )
                }
            }
        }
    }
}
