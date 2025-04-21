package com.github.openstream.core.model.playlist

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata

open class LocalPlaylist(
    val id: Int,
    override val items: SnapshotStateList<DataItem>,
    override val metadata: PlaylistMetadata,
): Playlist
