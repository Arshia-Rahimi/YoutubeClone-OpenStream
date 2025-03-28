package com.github.freetube.ui.global.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.playlist.PlaylistResult
import com.github.freetube.ui.designsystem.components.DataItemList
import com.github.freetube.ui.designsystem.components.ErrorPage
import com.github.freetube.ui.designsystem.components.LoadingBox
import com.github.freetube.ui.global.playlist.components.PlaylistTopBar

@Composable
fun PlaylistScreen(
    screenModel: PlaylistScreenModel,
    topBar: (@Composable () -> Unit) -> Unit,
    playVideo: (String) -> Unit,
    navigateBack: () -> Unit,
    toChannelScreen: (String) -> Unit,
) {
    val uiState by screenModel.state.collectAsStateWithLifecycle()

    when (uiState) {
        is PlaylistScreenModel.UiState.Loading -> {
            topBar {}
            LoadingBox()
        }

        is PlaylistScreenModel.UiState.Error -> ErrorPage(
            (uiState as PlaylistScreenModel.UiState.Error).message,
            navigateBack
        )

        is PlaylistScreenModel.UiState.Success -> {
            PlaylistScreen(
                playlist = (uiState as PlaylistScreenModel.UiState.Success).playlistResult,
                items = screenModel.items,
                topBar = topBar,
                playVideo = playVideo,
                toChannelScreen = toChannelScreen,
                loadNextPage = { screenModel.getNextPage() }
            )
        }
    }
}

@Composable
private fun PlaylistScreen(
    playlist: PlaylistResult,
    items: SnapshotStateList<DataItem>,
    toChannelScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
    loadNextPage: () -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar { PlaylistTopBar(playlist, toChannelScreen) }

    DataItemList(
        items = items,
        toChannelScreen = { toChannelScreen(it) },
        playVideo = { playVideo(it) },
        loadNextPage = loadNextPage,
    )
}
