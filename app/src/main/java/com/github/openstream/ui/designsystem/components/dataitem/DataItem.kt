package com.github.openstream.ui.designsystem.components.dataitem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.designsystem.components.dataitem.components.Channel
import com.github.openstream.ui.designsystem.components.dataitem.components.Playlist
import com.github.openstream.ui.designsystem.components.dataitem.components.Video

@Composable
fun DataItem(
    modifier: Modifier,
    item: DataItem,
    shouldViewChannel: Boolean = true,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (DataItem.Playlist) -> Unit,
    playVideo: (String) -> Unit,
    subscribe: (String) -> Unit = {},
    savePlaylist: (DataItem.Playlist) -> Unit = {},
    deletePlaylist: (DataItem.Playlist) -> Unit = {},
    addToWatchLater: ((DataItem.Video) -> Unit)? = null,
    removeFromWatchLater: ((DataItem.Video) -> Unit)? = null,
) {
    when(item) {
        is DataItem.Video -> Video(
            modifier = modifier,
            item = item,
            toChannelScreen = toChannelScreen,
            playVideo = playVideo,
            shouldViewChannel = shouldViewChannel,
            saveToWatchLater = addToWatchLater,
            removeFromWatchLater = removeFromWatchLater,
        )

        is DataItem.Playlist -> Playlist(
            modifier = modifier,
            item = item,
            toPlaylistScreen = toPlaylistScreen,
            toChannelScreen = toChannelScreen,
            shouldViewChannel = shouldViewChannel,
            savePlaylist = savePlaylist,
            deletePlaylist = deletePlaylist,
        )

        is DataItem.Channel -> Channel(
            modifier = modifier,
            item = item,
            toChannelScreen = toChannelScreen,
            subscribe = subscribe,
        )
    }
}
