package com.github.openstream.core.model.extractor

import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.model.dataitem.PlaylistItem
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

sealed interface PlaylistExtractor : Entityable {
    val data: PlaylistItem.YoutubePlaylistItem
    var extractor: YoutubePlaylistExtractor?
    var nextPage: Page?
}

data class OnlinePlaylistExtractor(
    override val data: PlaylistItem.YoutubePlaylistItem,
    override var extractor: YoutubePlaylistExtractor?,
    override var nextPage: Page? = null,
) : PlaylistExtractor {
    override fun toEntity() = PlaylistEntity(
        url = data.url,
        name = data.name,
        count = data.count,
        channelUrl = data.channelUrl,
        channelName = data.channelName,
        isChannelVerified = data.isChannelVerified,
    )

    fun toOfflineFirstPlaylist(id: Long) = OfflineFirstPlaylistExtractor(
        id = id,
        extractor = extractor,
        data = PlaylistItem.OfflineFirstPlaylistItem(
            name = data.name,
            thumbnail = data.thumbnail,
            count = data.count,
            id = id,
            channelName = data.channelName,
            channelUrl = data.channelUrl,
            isChannelVerified = data.isChannelVerified,
            url = data.url,
        ),
    )
}

data class OfflineFirstPlaylistExtractor(
    override val data: PlaylistItem.OfflineFirstPlaylistItem,
    override var extractor: YoutubePlaylistExtractor? = null,
    val id: Long, override var nextPage: Page? = null,
) : PlaylistExtractor {
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
