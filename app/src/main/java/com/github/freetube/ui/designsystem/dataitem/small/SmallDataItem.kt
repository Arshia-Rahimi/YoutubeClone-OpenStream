package com.github.freetube.ui.designsystem.dataitem.small

import androidx.compose.runtime.Composable
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.ui.designsystem.dataitem.small.components.SmallPlaylist
import com.github.freetube.ui.designsystem.dataitem.small.components.SmallVideo

@Composable
fun SmallDataItem(
    item: DataItem,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
) {
    when (item) {
        is DataItem.Video -> SmallVideo(
            item = item,
            playVideo = playVideo,
        )

        is DataItem.Playlist -> SmallPlaylist(
            item = item,
            toPlaylistScreen = toPlaylistScreen,
        )

        else -> {}
    }
}
