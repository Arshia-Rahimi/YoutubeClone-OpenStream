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
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
) {
    when(item) {
        is DataItem.Video -> Video(
            item = item,
            toChannelScreen = toChannelScreen,
            playVideo = playVideo,
        )

        is DataItem.Playlist -> Playlist(
            item = item,
            toPlaylistScreen = toPlaylistScreen,
            toChannelScreen = toChannelScreen,
        )

        is DataItem.Channel -> Channel(
            item = item,
            toChannelScreen = toChannelScreen,
        )
        is DataItem.Comment -> Comment(item)
    }
}
