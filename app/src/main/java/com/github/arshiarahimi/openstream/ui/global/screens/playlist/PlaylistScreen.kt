package com.github.arshiarahimi.openstream.ui.global.screens.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.DataItemList
import com.github.arshiarahimi.openstream.ui.global.screens.playlist.components.PlaylistTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PlaylistScreen(
    playlist: PlaylistItem,
    topBar: (@Composable () -> Unit) -> Unit,
    playVideo: (String) -> Unit,
    toChannelScreen: (String) -> Unit,
) {
    val viewModel = koinViewModel<PlaylistViewModel>(parameters = { parametersOf(playlist) })
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    PlaylistScreen(
        playlist = viewModel.playlist,
        topBar = topBar,
        playVideo = playVideo,
        items = viewModel.videos,
        toChannelScreen = toChannelScreen,
        loadNextPage = viewModel::getNextPage,
        onRefresh = viewModel::syncPlaylist,
        isRefreshing = isRefreshing,
    )
}

@Composable
private fun PlaylistScreen(
    playlist: PlaylistItem,
    items: SnapshotStateList<DataItem>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    toChannelScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
    loadNextPage: () -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar { PlaylistTopBar(playlist, toChannelScreen) }

    when (playlist) {
        is PlaylistItem.OfflineFirstPlaylistItem -> {
            DataItemList(
                onRefresh = onRefresh,
                isRefreshing = isRefreshing,
                items = items,
                toChannelScreen = toChannelScreen,
                playVideo = playVideo,
                loadNextPage = loadNextPage,
            )
        }

        else -> {
            DataItemList(
                items = items,
                toChannelScreen = toChannelScreen,
                playVideo = playVideo,
                loadNextPage = loadNextPage,
            )
        }
    }
}
