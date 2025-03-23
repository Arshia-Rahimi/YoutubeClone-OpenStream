package com.github.freetube.ui.global.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.core.extractor.channel.ChannelInfo
import com.github.freetube.core.extractor.channel.ChannelTab
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.ui.designsystem.LoadingBox
import com.github.freetube.ui.designsystem.dataitem.DataItem
import com.github.freetube.ui.global.channel.components.ChannelTopBar
import com.github.freetube.ui.global.channel.components.ErrorChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// todo needs refactoring

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
            trigger = { screenModel.onAction(it) },
            topBar = topBar,
            tabResults = tabResults,
            scope = scope,
            tabItems = screenModel.tabItems,
            navigateBack = navigateBack,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
private fun ChannelScreen(
    channelInfo: ChannelInfo,
    scope: CoroutineScope,
    tabResults: List<MutableState<ChannelTab>>?,
    tabItems: SnapshotStateList<SnapshotStateList<DataItem>>,
    trigger: (ChannelAction) -> Unit,
    navigateBack: () -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar { ChannelTopBar(channelInfo) }
    val pagerState = rememberPagerState { channelInfo.tabs.size }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
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
                        .padding(top = 4.dp, bottom = 8.dp)
                        .weight(1f / channelInfo.tabs.size)
                        .background(color)
                        .clickable { scope.launch { pagerState.scrollToPage(index) } }
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(tab.value.name, fontSize = 16.sp)
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            LaunchedEffect(page) { trigger(ChannelAction.GetTab(page)) }
            val currentItems = tabItems[page]
            when {
                // not recomposed
                tabResults?.get(page)?.value?.isLoading != false -> LoadingBox()
                tabResults[page].value.error != null -> ErrorChannel(tabResults[page].value.error) { navigateBack() }
                else -> {
                    val lazyColumnState = rememberLazyListState()
                    val shouldLoadNextPage by remember {
                        derivedStateOf { !lazyColumnState.canScrollForward && currentItems.isNotEmpty() }
                    }
                    LaunchedEffect(shouldLoadNextPage) {
                        if (shouldLoadNextPage) trigger(ChannelAction.GetTabNextPage(page))
                    }
                    LazyColumn(
                        state = lazyColumnState,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(
                            space = 8.dp,
                            alignment = Alignment.CenterVertically,
                        )
                    ) {
                        items(
                            currentItems,
                            key = { it.url + "-" + Uuid.random() },
                            contentType = { it }
                        ) {
                            DataItem(
                                item = it,
                                toChannelScreen = {},
                                toPlaylistScreen = {},
                                playVideo = {},
                            )
                        }
                        item {
                            if (currentItems.isNotEmpty()) Spacer(Modifier.height(48.dp))
                        }
                    }
                }
            }
        }
    }
}
