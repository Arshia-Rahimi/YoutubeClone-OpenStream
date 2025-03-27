package com.github.freetube.ui.designsystem.dataitem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.ui.designsystem.dataitem.components.Channel
import com.github.freetube.ui.designsystem.dataitem.components.Playlist
import com.github.freetube.ui.designsystem.dataitem.components.Video

@Composable
fun DataItem(
    modifier: Modifier,
    item: DataItem,
    shouldViewChannel: Boolean = true,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
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
        )
    }
}
