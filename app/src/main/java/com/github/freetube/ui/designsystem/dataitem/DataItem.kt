package com.github.freetube.ui.designsystem.dataitem

import androidx.compose.runtime.Composable
import com.github.freetube.core.extractor.DataItem
import com.github.freetube.ui.designsystem.dataitem.components.Channel
import com.github.freetube.ui.designsystem.dataitem.components.Comment
import com.github.freetube.ui.designsystem.dataitem.components.Playlist
import com.github.freetube.ui.designsystem.dataitem.components.Video

@Composable
fun DataItem(
    item: DataItem,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
) {
    when(item) {
        is DataItem.Video -> Video(
            item = item,
            toChannelScreen = toChannelScreen,
            playVideo = {
                // todo
            },
        )

        is DataItem.Playlist -> Playlist(
            item = item,
            toPlaylistScreen = toPlaylistScreen,
        )

        is DataItem.Channel -> Channel(
            item = item,
            toChannelScreen = toChannelScreen,
        )
        is DataItem.Comment -> Comment(item)
    }
}
