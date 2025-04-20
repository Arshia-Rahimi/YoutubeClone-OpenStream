package com.github.openstream.core.model.playlist

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.playlist.PlaylistMetadata

class OfflineFirstPlaylist(
    url: String,
    data: SnapshotStateList<DataItem>,
    metadata: PlaylistMetadata,
    hasNextPage: Boolean,
)