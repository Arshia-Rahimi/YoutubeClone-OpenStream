package com.github.freetube.ui.global.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.core.extractor.channel.ChannelInfo
import com.github.freetube.core.extractor.channel.ChannelTab
import com.github.freetube.ui.designsystem.LoadingBox
import com.github.freetube.ui.designsystem.dataitem.DataItem
import com.github.freetube.ui.global.channel.components.ChannelTopBar
import com.github.freetube.ui.global.channel.components.ErrorChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(
    screenModel: ChannelScreenModel,
    topBar: (@Composable () -> Unit) -> Unit,
    navigateBack: () -> Unit,
) {
    val uiState by screenModel.state.collectAsStateWithLifecycle()
    val tabResults by screenModel.tabResults
    val scope = rememberCoroutineScope()

    when (uiState) {
        is ChannelScreenModel.UiState.Loading -> LoadingBox()
        is ChannelScreenModel.UiState.Error -> ErrorChannel(
            (uiState as ChannelScreenModel.UiState.Error).message,
            navigateBack
        )
        is ChannelScreenModel.UiState.Success -> ChannelScreen(
            (uiState as ChannelScreenModel.UiState.Success).channelInfo,
            onAction = { screenModel.onAction(it) },
            topBar = topBar,
            tabResults = tabResults,
            scope = scope,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChannelScreen(
    channelInfo: ChannelInfo,
    scope: CoroutineScope,
    tabResults: List<ChannelTab>?,
    onAction: (ChannelAction) -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar { ChannelTopBar(channelInfo) }
    val pagerState = rememberPagerState { channelInfo.tabs.size }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
    ) {
        tabResults?.forEachIndexed { index, tab ->
            val color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.secondary
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .weight(1f / channelInfo.tabs.size)
                    .background(color)
                    .clickable { scope.launch { pagerState.scrollToPage(index) } }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(tab.name, fontSize = 16.sp)
            }
        }
    }
    HorizontalPager(
        state = pagerState,
    ) { page ->
        LaunchedEffect(page) { onAction(ChannelAction.GetTab(page)) }
        val tab by remember { derivedStateOf { tabResults?.get(page) } }
        val tabItems by remember { derivedStateOf { tab?.items ?: emptyList() } }
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterVertically,
            )
        ) {
            items(tabItems, key = { it.url }, contentType = { it }) {
                DataItem(
                    item = it,
                    toChannelScreen = {},
                    toPlaylistScreen = {},
                    playVideo = {},
                )
            }
            item {
                if (tabItems.isNotEmpty()) Spacer(Modifier.height(48.dp))
            }
        }
    }
}
