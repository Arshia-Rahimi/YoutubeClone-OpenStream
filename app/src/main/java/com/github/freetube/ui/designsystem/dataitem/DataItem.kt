package com.github.freetube.ui.designsystem.dataitem

import androidx.compose.runtime.Composable
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.ui.designsystem.dataitem.components.Channel
import com.github.freetube.ui.designsystem.dataitem.components.Comment
import com.github.freetube.ui.designsystem.dataitem.components.Playlist
import com.github.freetube.ui.designsystem.dataitem.components.Video

@Composable
fun DataItem(
    item: DataItem,
    shouldViewChannel: Boolean = true,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
) {
    when(item) {
        is DataItem.Video -> Video(
            item = item,
            toChannelScreen = toChannelScreen,
            playVideo = playVideo,
            shouldViewChannel = shouldViewChannel,
        )

        is DataItem.Playlist -> Playlist(
            item = item,
            toPlaylistScreen = toPlaylistScreen,
            toChannelScreen = toChannelScreen,
            shouldViewChannel = shouldViewChannel,
        )

        is DataItem.Channel -> Channel(
            item = item,
            toChannelScreen = toChannelScreen,
        )
        is DataItem.Comment -> Comment(item)
    }
}
