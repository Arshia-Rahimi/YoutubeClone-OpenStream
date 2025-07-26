package com.github.openstream.ui.designsystem.components.dataitem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.ui.designsystem.components.dataitem.components.Channel
import com.github.openstream.ui.designsystem.components.dataitem.components.Playlist
import com.github.openstream.ui.designsystem.components.dataitem.components.Video

@Composable
fun DataItem(
    modifier: Modifier,
    item: DataItem,
    shouldViewChannel: Boolean = true,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (PlaylistItem) -> Unit,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit = {},
    savePlaylist: (PlaylistItem.OnlinePlaylistItem) -> Unit = {},
    addToWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromPlaylist: ((VideoItem) -> Unit)? = null,
) {
    when (item) {
        is VideoItem -> Video(
            modifier = modifier,
            item = item,
            toChannelScreen = toChannelScreen,
            shouldViewChannel = shouldViewChannel,
            saveToWatchLater = addToWatchLater,
            removeFromWatchLater = removeFromWatchLater,
            removeFromPlaylist = removeFromPlaylist,
        )

        is PlaylistItem -> Playlist(
            modifier = modifier,
            item = item,
            toPlaylistScreen = toPlaylistScreen,
            toChannelScreen = toChannelScreen,
            shouldViewChannel = shouldViewChannel,
            savePlaylist = savePlaylist,
        )

        is ChannelItem -> Channel(
            modifier = modifier,
            item = item,
            toChannelScreen = toChannelScreen,
            subscribe = subscribe,
        )
    }
}
