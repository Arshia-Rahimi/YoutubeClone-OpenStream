package com.github.openstream.core.model.playlist

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.playlist.PlaylistMetadata

sealed interface Playlist {
    // unique playlist identification, title for local/offlineFirst ans url for youtube playlists
    val id: String
    val items: SnapshotStateList<DataItem>
    val metadata: PlaylistMetadata
    
    fun getNextPage()
}
