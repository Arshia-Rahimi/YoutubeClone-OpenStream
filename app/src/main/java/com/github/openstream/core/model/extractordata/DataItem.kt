package com.github.openstream.core.model.extractordata

import com.github.openstream.core.database.Entityable
import com.github.openstream.core.database.entities.ChannelEntity
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.VideoEntity
import kotlinx.serialization.Serializable

sealed interface DataItem : Entityable {
    val name: String
    val key: String
}

enum class StreamType {
    NORMAL, LIVE_STREAM, POST_LIVE_STREAM
}

data class VideoItem(
    override val name: String,
    val thumbnail: String?,
    val url: String,
    val streamType: StreamType,
    val channelName: String,
    val shortDescription: String?,
    val uploadDate: Long?,
    val viewCount: Long,
    val duration: Long,
    val channelUrl: String,
    val channelVerified: Boolean,
    val isShort: Boolean,
    val channelAvatars: String?,
    val id: Long? = null,
) : DataItem {
    override fun toEntity(): VideoEntity = VideoEntity(
        name = name,
        url = url,
        thumbnail = thumbnail,
        streamType = streamType,
        uploadDate = uploadDate,
        viewCount = viewCount,
        duration = duration,
        channelUrl = channelUrl,
        channelName = channelName,
        isChannelVerified = channelVerified,
    )

    override val key: String
        get() = "video-$url"
}

@Serializable
sealed interface PlaylistItem : DataItem {

    val thumbnail: String?
    val count: Long

    override fun toEntity(): PlaylistEntity

    @Serializable
    sealed interface LocalPlaylistItem : PlaylistItem {
        override val name: String
        override val thumbnail: String?
        override val count: Long
        val id: Long
        override val key: String
            get() = "playlist-$id"
    }

    @Serializable
    sealed interface YoutubePlaylistItem : PlaylistItem {
        val channelName: String
        val channelUrl: String
        val isChannelVerified: Boolean
        val url: String
    }

    @Serializable
    class LocalOnlyPlaylistItem(
        override val name: String,
        override val thumbnail: String?,
        override val count: Long,
        override val id: Long,
    ) : LocalPlaylistItem {
        override fun toEntity() =
            PlaylistEntity(
                name = name,
                count = count,
                thumbnail = thumbnail ?: "",
                playlistId = id,
            )
    }

    @Serializable
    class OfflineFirstPlaylistItem(
        override val name: String,
        override val thumbnail: String?,
        override val count: Long,
        override val id: Long,
        override val channelName: String,
        override val channelUrl: String,
        override val isChannelVerified: Boolean,
        override val url: String,
    ) : LocalPlaylistItem, YoutubePlaylistItem {
        override fun toEntity() = PlaylistEntity(
            name = name,
            channelUrl = channelUrl,
            channelName = channelName,
            count = count,
            isChannelVerified = isChannelVerified,
            url = url,
            thumbnail = thumbnail ?: "",
            playlistId = id,
        )
    }

    @Serializable
    data class OnlinePlaylistItem(
        override val name: String,
        override val thumbnail: String?,
        override val count: Long,
        override val channelName: String,
        override val channelUrl: String,
        override val isChannelVerified: Boolean,
        override val url: String,
    ) : YoutubePlaylistItem {
        override val key: String
            get() = "playlist-$url"

        override fun toEntity() =
            PlaylistEntity(
                name = name,
                channelUrl = channelUrl,
                channelName = channelName,
                count = count,
                isChannelVerified = isChannelVerified,
                url = url,
                thumbnail = thumbnail ?: "",
            )
    }
}

data class ChannelItem(
    override val name: String,
    val url: String,
    val thumbnail: String?,
    val description: String,
    val subscriberCount: Long,
    val verified: Boolean,
) : DataItem {
    override val key: String
        get() = "channel-$url"

    override fun toEntity(): ChannelEntity = ChannelEntity(
        name = name,
        url = url,
        isVerified = verified,
        subscriberCount = subscriberCount,
        description = description,
        avatar = thumbnail,
        banner = "",
    )
}
