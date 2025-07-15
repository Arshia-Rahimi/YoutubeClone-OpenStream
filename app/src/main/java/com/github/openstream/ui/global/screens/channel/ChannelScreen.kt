package com.github.openstream.ui.global.screens.channel

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
import androidx.compose.material3.Scaffold
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
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.ui.designsystem.components.ErrorPage
import com.github.openstream.ui.designsystem.components.LoadingBox
import com.github.openstream.ui.designsystem.components.dataitem.DataItemList
import com.github.openstream.ui.global.screens.channel.components.ChannelTopBar
import com.github.openstream.ui.global.screens.channel.model.ChannelTabView
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(
    url: String,
    navigateBack: () -> Unit,
    toPlaylistScreen: (PlaylistItem) -> Unit,
) {
    val viewModel = koinViewModel<ChannelViewModel>(
        parameters = { parametersOf(url) },
        key = url,
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabResults = viewModel.tabs


    when (uiState) {
        is ChannelViewModel.UiState.Loading -> LoadingBox()

        is ChannelViewModel.UiState.Error -> ErrorPage(
            (uiState as ChannelViewModel.UiState.Error).message,
            navigateBack
        )

        is ChannelViewModel.UiState.Success -> ChannelScreen(
            channelItem = viewModel.channelItem,
            tabResults = tabResults,
            tabItems = viewModel.tabItems,
            navigateBack = navigateBack,
            toPlaylistScreen = toPlaylistScreen,
            getTabNextPage = viewModel::getTabNextPage,
            getTabFirstPage = viewModel::getTabFirstPage,
            addToWatchLater = viewModel::addToWatchLater,
            savePlaylist = viewModel::savePlaylist,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
private fun ChannelScreen(
    channelItem: ChannelItem,
    tabResults: SnapshotStateList<ChannelTabView>,
    tabItems: SnapshotStateList<SnapshotStateList<DataItem>>,
    navigateBack: () -> Unit,
    toPlaylistScreen: (PlaylistItem) -> Unit,
    getTabFirstPage: (ChannelTabView) -> Unit,
    getTabNextPage: (ChannelTabView) -> Unit,
    addToWatchLater: (VideoItem) -> Unit,
    savePlaylist: (PlaylistItem.OnlinePlaylistItem) -> Unit,
) {
    val pagerState = rememberPagerState { tabResults.size }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ChannelTopBar(channelItem) { isBottomSheetVisible = true }
        }
    ) { ip ->
        if (tabResults.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(ip),
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 16.dp,
                        alignment = Alignment.CenterHorizontally,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    itemsIndexed(tabResults) { index, tab ->
                        val isSelected = index == pagerState.currentPage
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { scope.launch { pagerState.scrollToPage(index) } }
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                tab.name,
                                fontSize = 16.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary,
                            )
                        }
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) { page ->
                    val currentTab = tabResults[page]
                    LaunchedEffect(page) { getTabFirstPage(currentTab) }

                    when {
                        currentTab.isLoading -> LoadingBox()
                        currentTab.error != null -> ErrorPage(currentTab.error) { navigateBack() }
                        else -> DataItemList(
                            items = tabItems[page],
                            shouldViewChannel = false,
                            loadNextPage = { getTabNextPage(currentTab) },
                            toPlaylistScreen = toPlaylistScreen,
                            lazyListUniqueId = "channelScreen/${currentTab.name}",
                            addToWatchLater = addToWatchLater,
                            savePlaylist = savePlaylist,
                        )
                    }
                }
            }
        }
    }
}
