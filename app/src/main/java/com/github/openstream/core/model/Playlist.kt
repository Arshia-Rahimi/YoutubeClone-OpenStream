package com.github.openstream.core.model

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

sealed interface Playlist {
    var items: ImmutableArray<DataItem.Video>
    val metadata: PlaylistMetadata
}

sealed interface YoutubePlaylist : Playlist {
    val url: String
    var nextPage: Page?
    var extractor: YoutubePlaylistExtractor?
}

open class LocalPlaylist(
    val id: Int,
    override var items: ImmutableArray<DataItem.Video>,
    override val metadata: PlaylistMetadata,
) : Playlist {
    fun toEntity(): PlaylistEntity = PlaylistEntity(
        id = id,
        name = metadata.name,
        count = metadata.count,
        channelUrl = metadata.channelUrl,
        channelName = metadata.channelName,
        isChannelVerified = metadata.isChannelVerified,
    )
}

class OnlinePlaylist(
    override val url: String,
    override var items: ImmutableArray<DataItem.Video>,
    override val metadata: PlaylistMetadata,
    override var extractor: YoutubePlaylistExtractor?,
    override var nextPage: Page?
) : YoutubePlaylist

class OfflineFirstPlaylist(
    override val url: String,
    override var nextPage: Page? = null,
    override var extractor: YoutubePlaylistExtractor? = null,
    id: Int,
    items: ImmutableArray<DataItem.Video>,
    metadata: PlaylistMetadata,
) : LocalPlaylist(
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
