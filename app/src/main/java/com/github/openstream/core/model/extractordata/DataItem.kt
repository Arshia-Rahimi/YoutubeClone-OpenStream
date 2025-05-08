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

data class Video(
    override val name: String,
    val thumbnail: String?,
    val url: String,
    val streamType: StreamType,
    val channelName: String,
    val shortDescription: String?,
    val uploadOffset: String,
    val uploadDate: String,
    val viewCount: Long,
    val duration: Long,
    val channelUrl: String,
    val channelVerified: Boolean,
    val isShort: Boolean,
    val channelAvatars: String?,
    val id: Long? = null,
) : DataItem {
    override fun toEntity() = VideoEntity(
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
sealed interface Playlist : DataItem {

    val thumbnail: String?
    val count: Long

    @Serializable
    sealed interface LocalPlaylist : Playlist {
        override val name: String
        override val thumbnail: String?
        override val count: Long
        val id: Long
        override val key: String
            get() = "playlist-$id"
    }

    @Serializable
    class LocalOnlyPlaylist(
        override val name: String,
        override val thumbnail: String?,
        override val count: Long,
        override val id: Long,
    ) : LocalPlaylist {
        override fun toEntity() =
            PlaylistEntity(
                name = name,
                count = count,
                thumbnail = thumbnail ?: "",
                playlistId = id,
            )
    }

    @Serializable
    class OfflineFirstPlaylist(
        override val name: String,
        override val thumbnail: String?,
        override val count: Long,
        override val id: Long,
        val channelName: String,
        val channelUrl: String,
        val isChannelVerified: Boolean,
        val url: String,
    ) : LocalPlaylist {
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
    data class OnlinePlaylist(
        override val name: String,
        override val thumbnail: String?,
        override val count: Long,
        val channelName: String,
        val channelUrl: String,
        val isChannelVerified: Boolean,
        val url: String,
    ) : Playlist {
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

data class Channel(
    override val name: String,
    val url: String,
    val thumbnail: String?,
    val description: String,
    val subscriberCount: Long,
    val verified: Boolean,
) : DataItem {
    override val key: String
        get() = "channel-$url"

    override fun toEntity() = ChannelEntity(
        name = name,
        url = url,
        isVerified = verified,
        subscriberCount = subscriberCount,
        description = description,
        avatar = thumbnail,
        banner = "",
    )
}
