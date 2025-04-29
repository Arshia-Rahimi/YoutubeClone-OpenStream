package com.github.openstream.ui.global.shared.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import com.github.openstream.ui.designsystem.components.DataItemList
import com.github.openstream.ui.designsystem.components.ErrorPage
import com.github.openstream.ui.designsystem.components.LoadingBox
import com.github.openstream.ui.global.shared.playlist.components.PlaylistTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterSetOf

@Composable
fun PlaylistScreen(
    playlist: DataItem.Playlist,
    topBar: (@Composable () -> Unit) -> Unit,
    playVideo: (String) -> Unit,
    navigateBack: () -> Unit,
    toChannelScreen: (String) -> Unit,
) {
    topBar {}
    val viewModel = koinViewModel<PlaylistViewModel>(parameters = { parameterSetOf(playlist) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                toChannelScreen = toChannelScreen,
                loadNextPage = { viewModel.getNextPage() }
            )
        }
    }
}

@Composable
private fun PlaylistScreen(
    playlist: Playlist,
    toChannelScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
    loadNextPage: () -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar { PlaylistTopBar(playlist.metadata, toChannelScreen) }

    DataItemList(
        items = playlist.items,
        toChannelScreen = { toChannelScreen(it) },
        playVideo = { playVideo(it) },
        loadNextPage = loadNextPage,
    )
}
