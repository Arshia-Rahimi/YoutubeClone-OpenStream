package com.github.arshiarahimi.openstream.ui.global.screens.channel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelMetadata
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTab
import com.github.arshiarahimi.openstream.core.model.extractordata.DataItem
import com.github.arshiarahimi.openstream.core.model.extractordata.PlaylistItem
import com.github.arshiarahimi.openstream.ui.designsystem.components.ErrorPage
import com.github.arshiarahimi.openstream.ui.designsystem.components.LoadingBox
import kotlinx.coroutines.CoroutineScope
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
    val tabResults by viewModel.tabResults
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
            (uiState as ChannelViewModel.UiState.Success).channelInfo,
            trigger = viewModel::onAction,
            topBar = topBar,
            tabResults = tabResults,
            scope = scope,
            tabItems = viewModel.tabItems,
            navigateBack = navigateBack,
            playVideo = playVideo,
            toPlaylistScreen = toPlaylistScreen,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
private fun ChannelScreen(
    channelInfo: ChannelMetadata,
    scope: CoroutineScope,
    tabResults: List<MutableState<ChannelTab>>?,
    tabItems: SnapshotStateList<SnapshotStateList<DataItem>>,
    trigger: (ChannelAction) -> Unit,
    navigateBack: () -> Unit,
    playVideo: (String) -> Unit,
    toPlaylistScreen: (PlaylistItem) -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {
//    val pagerState = rememberPagerState { channelInfo.tabs.size }
//    var isBottomSheetVisible by remember { mutableStateOf(false) }
//    topBar { ChannelTopBar(channelInfo) { isBottomSheetVisible = true } }
//    if (isBottomSheetVisible) {
//        ModalBottomSheet(
//            onDismissRequest = { isBottomSheetVisible = false },
//        ) {
//            Box(
//                contentAlignment = Alignment.TopCenter,
//                modifier = Modifier.padding(8.dp)
//            ) {
//                Text(text = channelInfo.description, fontSize = 16.sp)
//            }
//        }
//    }
//    Column(
//        modifier = Modifier.fillMaxSize(),
//    ) {
//        tabResults?.let {
//            LazyRow(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight(),
//                horizontalArrangement = Arrangement.Center,
//            ) {
//                itemsIndexed(tabResults) { index, tab ->
//                    val color =
//                        if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
//                        else MaterialTheme.colorScheme.secondary
//                    Box(
//                        modifier = Modifier
//                            .padding(top = 4.dp, bottom = 8.dp)
//                            .background(color)
//                            .clickable { scope.launch { pagerState.scrollToPage(index) } }
//                            .padding(horizontal = 8.dp),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        Text(tab.value.name, fontSize = 16.sp)
//                    }
//                }
//            }
//        }
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier.weight(1f),
//        ) { page ->
//            LaunchedEffect(page) { trigger(ChannelAction.GetTab(page)) }
//            val currentItems = tabItems[page]
//            when {
//                tabResults?.get(page)?.value?.isLoading != false -> LoadingBox()
//                tabResults[page].value.error != null -> ErrorPage(tabResults[page].value.error) { navigateBack() }
//                else -> {
//                    DataItemList(
//                        items = currentItems,
//                        shouldViewChannel = false,
//                        loadNextPage = { trigger(ChannelAction.GetTabNextPage(page)) },
//                        playVideo = playVideo,
//                        toPlaylistScreen = toPlaylistScreen,
//                    ) 
//                }
//            }
//        }
//    }
}
