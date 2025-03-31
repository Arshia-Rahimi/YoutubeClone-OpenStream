package com.github.freetube.core.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.playlist.PlaylistResult

abstract class Playlist(
    val url: String,
    val name: String,
) {
    abstract val data: SnapshotStateList<DataItem>
    abstract val metaData: PlaylistResult
    abstract val hasNextPage: Boolean
    abstract val nextPage: Any
}
