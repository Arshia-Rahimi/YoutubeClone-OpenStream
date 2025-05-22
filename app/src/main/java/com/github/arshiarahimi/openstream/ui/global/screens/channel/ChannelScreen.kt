package com.github.arshiarahimi.openstream.ui.global.screens.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTabView
import com.github.arshiarahimi.openstream.ui.designsystem.components.ErrorPage
import com.github.arshiarahimi.openstream.ui.designsystem.components.LoadingBox
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.DataItemList
import com.github.arshiarahimi.openstream.ui.global.screens.channel.components.ChannelTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(
    url: String,
    topBar: (@Composable () -> Unit) -> Unit,
    playVideo: (String) -> Unit,
    navigateBack: () -> Unit,
    toPlaylistScreen: (PlaylistItem) -> Unit,
) {
    topBar {}
    val viewModel = koinViewModel<ChannelViewModel>(parameters = { parametersOf(url) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabResults = viewModel.tabs
    val scope = rememberCoroutineScope()

    when (uiState) {
        is ChannelViewModel.UiState.Loading -> {
            topBar {}
            LoadingBox()
        }

        is ChannelViewModel.UiState.Error -> ErrorPage(
            (uiState as ChannelViewModel.UiState.Error).message,
            navigateBack
        )

        is ChannelViewModel.UiState.Success -> ChannelScreen(
            channelItem = viewModel.channelItem,
            topBar = topBar,
            tabResults = tabResults,
            scope = scope,
            tabItems = viewModel.tabItems,
            navigateBack = navigateBack,
            playVideo = playVideo,
            toPlaylistScreen = toPlaylistScreen,
            getTabNextPage = viewModel::getTabNextPage,
            getTabFirstPage = viewModel::getTabFirstPage,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
private fun ChannelScreen(
    channelItem: ChannelItem,
    scope: CoroutineScope,
    tabResults: SnapshotStateList<ChannelTabView>,
    tabItems: SnapshotStateList<SnapshotStateList<DataItem>>,
    navigateBack: () -> Unit,
    playVideo: (String) -> Unit,
    toPlaylistScreen: (PlaylistItem) -> Unit,
    getTabFirstPage: (ChannelTabView) -> Unit,
    getTabNextPage: (ChannelTabView) -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    val pagerState = rememberPagerState { tabResults.size }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    topBar { ChannelTopBar(channelItem) { isBottomSheetVisible = true } }
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible = false },
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = channelItem.description, fontSize = 16.sp)
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
        ) {
            itemsIndexed(tabResults) { index, tab ->
                val color =
                    if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 8.dp)
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
            modifier = Modifier.weight(1f),
        ) { page ->
            val currentTab = tabResults[page]
            val currentItems = tabItems[page]
            LaunchedEffect(page) { getTabFirstPage(currentTab) }

            when {
                currentTab.isLoading -> LoadingBox()
                currentTab.error != null -> ErrorPage(currentTab.error) { navigateBack() }
                else -> {
                    DataItemList(
                        items = currentItems,
                        shouldViewChannel = false,
                        loadNextPage = { getTabNextPage(currentTab) },
                        playVideo = playVideo,
                        toPlaylistScreen = toPlaylistScreen,
                    )
                }
            }
        }
    }
}
