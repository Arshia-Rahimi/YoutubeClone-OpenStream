package com.github.openstream.core.model.extractordata

import com.github.openstream.core.database.Entityable
import com.github.openstream.core.database.entities.PlaylistEntity
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

sealed interface YoutubePlaylist : Entityable, ViewableObject {
    val data: PlaylistItem.YoutubePlaylistItem
    var extractor: YoutubePlaylistExtractor?
    var nextPage: Page?
}

data class OnlinePlaylist(
    override val data: PlaylistItem.YoutubePlaylistItem,
    override var extractor: YoutubePlaylistExtractor?,
    override var nextPage: Page? = null,
) : YoutubePlaylist {
    override fun toEntity() = PlaylistEntity(
        url = data.url,
        name = data.name,
        count = data.count,
        channelUrl = data.channelUrl,
        channelName = data.channelName,
        isChannelVerified = data.isChannelVerified,
    )
    
    fun toOfflineFirstPlaylist(id: Long) = OfflineFirstPlaylist(
        id = id,
        data = data,
        extractor = extractor,
    )
}

data class OfflineFirstPlaylist(
    override val data: PlaylistItem.YoutubePlaylistItem,
    override var extractor: YoutubePlaylistExtractor? = null,
    val id: Long, override var nextPage: Page? = null,
) : YoutubePlaylist {
    override fun toEntity() = PlaylistEntity(
        url = data.url,
        playlistId = id,
        name = data.name,
        count = data.count,
        channelUrl = data.channelUrl,
        channelName = data.channelName,
        isChannelVerified = data.isChannelVerified,
    )
}
