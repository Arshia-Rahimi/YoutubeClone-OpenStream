package com.github.arshiarahimi.openstream.ui.global.screens.playlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.arshiarahimi.openstream.core.common.compose.ObserveForEvents
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.shared.DefaultPlaylists
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.DataItemList
import com.github.arshiarahimi.openstream.ui.global.screens.playlist.components.PlaylistTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PlaylistScreen(
    playlist: PlaylistItem,
    toChannelScreen: (String) -> Unit,
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<PlaylistViewModel>(
        parameters = { parametersOf(playlist) },
        key = playlist.hashCode().toString(),
    )
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val playlist by viewModel.playlist.collectAsStateWithLifecycle()
    
    ObserveForEvents(viewModel.navBack) {
        navigateBack()
    }
    
    PlaylistScreen(
        playlist = playlist,
        items = viewModel.videos,
        toChannelScreen = toChannelScreen,
        loadNextPage = viewModel::getNextPage,
        onRefresh = viewModel::syncPlaylist,
        isRefreshing = isRefreshing,
        removeFromPlaylist = viewModel::removeFromPlaylist,
        addToWatchLater = viewModel::addToWatchLater,
    )
}

@Composable
private fun PlaylistScreen(
    playlist: PlaylistItem,
    items: SnapshotStateList<DataItem>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    toChannelScreen: (String) -> Unit,
    loadNextPage: () -> Unit,
    removeFromPlaylist: (VideoItem) -> Unit,
    addToWatchLater: (VideoItem) -> Unit,
) {
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            PlaylistTopBar(playlist, toChannelScreen)
        }
    ) { ip ->
        when (playlist) {
            is PlaylistItem.OfflineFirstPlaylistItem -> {
                DataItemList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(ip),
                    onRefresh = onRefresh,
                    isRefreshing = isRefreshing,
                    items = items,
                    toChannelScreen = toChannelScreen,
                    loadNextPage = loadNextPage,
                )
            }

            is PlaylistItem.LocalOnlyPlaylistItem -> {
                if (playlist.id == DefaultPlaylists.WATCH_LATER_ID) {
                    DataItemList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(ip),
                        items = items,
                        toChannelScreen = toChannelScreen,
                        loadNextPage = loadNextPage,
                        removeFromPlaylist = removeFromPlaylist,
                    )
                } else {
                    DataItemList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(ip),
                        items = items,
                        toChannelScreen = toChannelScreen,
                        loadNextPage = loadNextPage,
                        removeFromPlaylist = removeFromPlaylist,
                        addToWatchLater = addToWatchLater,
                    )
                }
            }

            is PlaylistItem.OnlinePlaylistItem -> {
                DataItemList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(ip),
                    items = items,
                    toChannelScreen = toChannelScreen,
                    loadNextPage = loadNextPage,
                )
            }
        }
    }
}
