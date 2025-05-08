package com.github.openstream.core.model

import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

sealed interface Playlist {
    var items: Array<DataItem.Video>
    val metadata: PlaylistMetadata

    fun toEntity(): PlaylistEntity
}

sealed interface LocalPlaylist : Playlist {
    val id: Long
}

class LocalOnlyPlaylist(
    override val id: Long,
    override var items: Array<DataItem.Video>,
    override val metadata: PlaylistMetadata,
) : LocalPlaylist {
    override fun toEntity() = PlaylistEntity(
        playlistId = id,
        name = metadata.name,
        count = metadata.count,
        channelUrl = metadata.channelUrl,
        channelName = metadata.channelName,
        isChannelVerified = metadata.isChannelVerified,
    )
}

sealed interface YoutubePlaylist : Playlist {
    val url: String
    var nextPage: Page?
    var extractor: YoutubePlaylistExtractor?
}

class OnlinePlaylist(
    override val url: String,
    override var items: Array<DataItem.Video>,
    override val metadata: PlaylistMetadata,
    override var extractor: YoutubePlaylistExtractor?,
    override var nextPage: Page?
) : YoutubePlaylist {
    override fun toEntity() = PlaylistEntity(
        url = url,
        name = metadata.name,
        count = metadata.count,
        channelUrl = metadata.channelUrl,
        channelName = metadata.channelName,
        isChannelVerified = metadata.isChannelVerified,
    )
}

class OfflineFirstPlaylist(
    override val url: String,
    override var nextPage: Page? = null,
    override var extractor: YoutubePlaylistExtractor? = null,
    override val id: Long,
    override var items: Array<DataItem.Video>,
    override val metadata: PlaylistMetadata,
) : LocalPlaylist, YoutubePlaylist {
    override fun toEntity() = PlaylistEntity(
        url = url,
        playlistId = id,
        name = metadata.name,
        count = metadata.count,
        channelUrl = metadata.channelUrl,
        channelName = metadata.channelName,
        isChannelVerified = metadata.isChannelVerified,
    )
}

fun OnlinePlaylist.toOfflineFirstPlaylist(id: Long) = OfflineFirstPlaylist(
    url = url,
    items = items,
    metadata = metadata,
    id = id,
    nextPage = nextPage,
)
