package com.github.freetube.ui.global.channel

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
import androidx.compose.runtime.MutableState
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
import com.github.freetube.core.extractor.channel.ChannelInfo
import com.github.freetube.core.extractor.channel.ChannelTab
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.ui.designsystem.components.DataItemList
import com.github.freetube.ui.designsystem.components.ErrorPage
import com.github.freetube.ui.designsystem.components.LoadingBox
import com.github.freetube.ui.global.channel.components.ChannelTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(
    screenModel: ChannelScreenModel,
    topBar: (@Composable () -> Unit) -> Unit,
    playVideo: (String) -> Unit,
    navigateBack: () -> Unit,
    toPlaylistScreen: (String) -> Unit,
) {
    val uiState by screenModel.state.collectAsStateWithLifecycle()
    val tabResults by screenModel.tabResults
    val scope = rememberCoroutineScope()

    when (uiState) {
        is ChannelScreenModel.UiState.Loading -> {
            topBar {}
            LoadingBox()
        }
        is ChannelScreenModel.UiState.Error -> ErrorPage(
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
            playVideo = playVideo,
            toPlaylistScreen = toPlaylistScreen,
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
    playVideo: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    val pagerState = rememberPagerState { channelInfo.tabs.size }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    topBar { ChannelTopBar(channelInfo) { isBottomSheetVisible = true } }
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible = false },
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = channelInfo.description, fontSize = 16.sp)
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        tabResults?.let {
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
                        Text(tab.value.name, fontSize = 16.sp)
                    }
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
                tabResults?.get(page)?.value?.isLoading != false -> LoadingBox()
                tabResults[page].value.error != null -> ErrorPage(tabResults[page].value.error) { navigateBack() }
                else -> {
                    DataItemList(
                        items = currentItems,
                        shouldViewChannel = false,
                        loadNextPage = { trigger(ChannelAction.GetTabNextPage(page)) },
                        playVideo = playVideo,
                        toPlaylistScreen = toPlaylistScreen,
                    ) 
                }
            }
        }
    }
}
