package com.github.openstream.core.model.playlist

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.playlist.PlaylistMetadata

class YoutubePlaylist(
    override val id: String,
    override val items: SnapshotStateList<DataItem>,
    override val metadata: PlaylistMetadata,
): Playlist {
    override fun getNextPage() {
        TODO("Not yet implemented")
    }
}
