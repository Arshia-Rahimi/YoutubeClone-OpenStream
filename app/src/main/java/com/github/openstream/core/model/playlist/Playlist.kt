package com.github.openstream.core.model.playlist

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata

sealed interface Playlist {
    val items: SnapshotStateList<DataItem>
    val metadata: PlaylistMetadata
}
