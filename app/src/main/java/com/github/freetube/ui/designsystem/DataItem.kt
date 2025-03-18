package com.github.freetube.ui.designsystem

import androidx.compose.runtime.Composable
import com.github.freetube.core.extractor.DataItem
import com.github.freetube.ui.designsystem.dataitems.Channel
import com.github.freetube.ui.designsystem.dataitems.Comment
import com.github.freetube.ui.designsystem.dataitems.Playlist
import com.github.freetube.ui.designsystem.dataitems.Video

@Composable
fun DataItem(
    item: DataItem,
) {
    when(item) {
        is DataItem.Video -> Video(item)
        is DataItem.Playlist -> Playlist(item)
        is DataItem.Channel -> Channel(item)
        is DataItem.Comment -> Comment(item)
    }
}
