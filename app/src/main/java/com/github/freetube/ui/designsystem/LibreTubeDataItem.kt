package com.github.freetube.ui.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.freetube.core.extractor.DataItem
import com.github.freetube.ui.designsystem.dataitems.Channel
import com.github.freetube.ui.designsystem.dataitems.Comment
import com.github.freetube.ui.designsystem.dataitems.Playlist
import com.github.freetube.ui.designsystem.dataitems.Video

@Composable
fun LibreTubeDataItem(
    modifier: Modifier = Modifier,
    item: DataItem,
) {
    when(item) {
        is DataItem.Video -> Video(modifier, item)
        is DataItem.Playlist -> Playlist(modifier, item)
        is DataItem.Channel -> Channel(modifier, item)
        is DataItem.Comment -> Comment(modifier, item)
    }
}
