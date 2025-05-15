package com.github.openstream.core.model.extractordata

import com.github.openstream.core.database.Entityable
import com.github.openstream.core.database.entities.PlaylistEntity
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

sealed interface Playlist : Entityable, ViewableObject {
    var items: List<VideoItem>
    val metadata: PlaylistMetadata
    
    override fun toEntity(): PlaylistEntity
}

sealed interface LocalPlaylist : Playlist {
    val id: Long
}

data class LocalOnlyPlaylist(
    override val id: Long,
    override var items: List<VideoItem>,
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

data class OnlinePlaylist(
    override val url: String,
    override var items: List<VideoItem>,
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
    
    fun toOfflineFirstPlaylist(id: Long) = OfflineFirstPlaylist(
        url = url,
        items = items,
        metadata = metadata,
        id = id,
        nextPage = nextPage,
    )
}

data class OfflineFirstPlaylist(
    override val url: String,
    override var nextPage: Page? = null,
    override var extractor: YoutubePlaylistExtractor? = null,
    override val id: Long,
    override var items: List<VideoItem>,
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
