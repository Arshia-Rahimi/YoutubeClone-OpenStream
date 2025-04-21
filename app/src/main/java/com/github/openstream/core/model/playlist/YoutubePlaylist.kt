package com.github.openstream.core.model.playlist

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

sealed interface YoutubePlaylist: Playlist {
   val url: String
   var nextPage: Page?
   var extractor: YoutubePlaylistExtractor?
}

class OnlinePlaylist(
    override val url: String,
    override val items: SnapshotStateList<DataItem>,
    override val metadata: PlaylistMetadata,
    override var extractor: YoutubePlaylistExtractor?,
    override var nextPage: Page?
): YoutubePlaylist

class OfflineFirstPlaylist(
    override val url: String,
    override var nextPage: Page? = null,
    override var extractor: YoutubePlaylistExtractor? = null,
    id: Int,
    items: SnapshotStateList<DataItem>,
    metadata: PlaylistMetadata,
): LocalPlaylist(
    id = id,
    items = items,
    metadata = metadata,
), YoutubePlaylist

fun OnlinePlaylist.toOfflineFirstPlaylist(id: Int) = OfflineFirstPlaylist(
    url = url,
    items = items,
    metadata = metadata,
    id = id,
    nextPage = nextPage,
)
