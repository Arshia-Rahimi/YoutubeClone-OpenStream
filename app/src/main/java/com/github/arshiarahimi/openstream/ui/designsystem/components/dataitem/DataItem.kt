package com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.components.Channel
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.components.Playlist
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.components.Video

@Composable
fun DataItem(
    modifier: Modifier,
    item: DataItem,
    shouldViewChannel: Boolean = true,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (PlaylistItem) -> Unit,
    playVideo: (String) -> Unit,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit = {},
    savePlaylist: (PlaylistItem.OnlinePlaylistItem) -> Unit = {},
    addToWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromWatchLater: ((VideoItem) -> Unit)? = null,
) {
    when (item) {
        is VideoItem -> Video(
            modifier = modifier,
            item = item,
            toChannelScreen = toChannelScreen,
            playVideo = playVideo,
            shouldViewChannel = shouldViewChannel,
            saveToWatchLater = addToWatchLater,
            removeFromWatchLater = removeFromWatchLater,
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
