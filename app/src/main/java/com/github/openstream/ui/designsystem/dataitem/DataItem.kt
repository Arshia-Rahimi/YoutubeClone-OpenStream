package com.github.openstream.ui.designsystem.dataitem

import android.provider.ContactsContract
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.designsystem.dataitem.components.Channel
import com.github.openstream.ui.designsystem.dataitem.components.Playlist
import com.github.openstream.ui.designsystem.dataitem.components.Video

@Composable
fun DataItem(
    modifier: Modifier,
    item: DataItem,
    shouldViewChannel: Boolean = true,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (DataItem.Playlist) -> Unit,
    playVideo: (String) -> Unit,
    subscribe: (String) -> Unit = {},
) {
    when(item) {
        is DataItem.Video -> Video(
            modifier = modifier,
            item = item,
            toChannelScreen = toChannelScreen,
            playVideo = playVideo,
            shouldViewChannel = shouldViewChannel,
        )

        is DataItem.Playlist -> Playlist(
            modifier = modifier,
            item = item,
            toPlaylistScreen = toPlaylistScreen,
            toChannelScreen = toChannelScreen,
            shouldViewChannel = shouldViewChannel,
        )

        is DataItem.Channel -> Channel(
            modifier = modifier,
            item = item,
            toChannelScreen = toChannelScreen,
            subscribe = subscribe,
        )
    }
}
