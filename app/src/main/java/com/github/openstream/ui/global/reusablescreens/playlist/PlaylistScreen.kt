package com.github.openstream.ui.global.reusablescreens.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.designsystem.components.ErrorPage
import com.github.openstream.ui.designsystem.components.LoadingBox
import com.github.openstream.ui.designsystem.dataitem.DataItemList
import com.github.openstream.ui.global.reusablescreens.playlist.components.PlaylistTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PlaylistScreen(
    playlist: DataItem.Playlist,
    topBar: (@Composable () -> Unit) -> Unit,
    playVideo: (String) -> Unit,
    navigateBack: () -> Unit,
    toChannelScreen: (String) -> Unit,
) {
    topBar {}
    val viewModel = koinViewModel<PlaylistViewModel>(parameters = { parametersOf(playlist) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    when (uiState) {
        is PlaylistViewModel.UiState.Loading -> {
            topBar {}
            LoadingBox()
        }

        is PlaylistViewModel.UiState.Error -> ErrorPage(
            (uiState as PlaylistViewModel.UiState.Error).message,
            navigateBack
        )

        is PlaylistViewModel.UiState.Success -> {
            PlaylistScreen(
                playlist = (uiState as PlaylistViewModel.UiState.Success).playlist,
                topBar = topBar,
                playVideo = playVideo,
                items = viewModel.items,
                toChannelScreen = toChannelScreen,
                loadNextPage = { viewModel.getNextPage() },
                onRefresh = { viewModel.syncPlaylist() },
                isRefreshing = isRefreshing,
            )
        }
    }
}

@Composable
private fun PlaylistScreen(
    playlist: Playlist,
    items: SnapshotStateList<DataItem>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    toChannelScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
    loadNextPage: () -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar { PlaylistTopBar(playlist.metadata, toChannelScreen) }

    DataItemList(
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        items = items,
        toChannelScreen = { toChannelScreen(it) },
        playVideo = { playVideo(it) },
        loadNextPage = loadNextPage,
    )
}
