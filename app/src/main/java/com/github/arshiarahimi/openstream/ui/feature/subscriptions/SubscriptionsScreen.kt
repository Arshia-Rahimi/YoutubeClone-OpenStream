package com.github.arshiarahimi.openstream.ui.feature.subscriptions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.arshiarahimi.openstream.R
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.DataItemList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    toPlaylistScreen: (PlaylistItem) -> Unit,
    toChannelScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
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
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .clickable {
                                scope.launch { pagerState.scrollToPage(tab.ordinal) }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(tab.title),
                            color = if (tab.ordinal == pagerState.currentPage) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary,
                        )
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (page) {
                    SubscriptionsPage.VIDEOS.ordinal ->
                        DataItemList(
                            items = viewModel.videos,
                            toChannelScreen = toChannelScreen,
                            toPlaylistScreen = toPlaylistScreen,
                            playVideo = playVideo,
                            isRefreshing = isRefreshing,
                            onRefresh = viewModel::updateSubscriptions,
                            addToWatchLater = viewModel::addToWatchLater,
                        )
                    
                    SubscriptionsPage.CHANNELS.ordinal ->
                        DataItemList(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(ip),
                            items = viewModel.subscriptions,
                            toChannelScreen = toChannelScreen,
                        )
                }
            }
        }
    }
}
