package com.github.openstream.core.model.extractordata

import com.github.openstream.core.database.Entityable
import com.github.openstream.core.database.entities.ChannelEntity
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.VideoEntity
import kotlinx.serialization.Serializable
import java.util.UUID

sealed class DataItem {

    data class Video(
        val name: String,
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
        val id: Long,
    ) : DataItem(), Entityable {
        override fun toEntity(): VideoEntity {
            return VideoEntity(
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
                id = id,
            )
        }
    }
    
    @Serializable
    sealed class Playlist : DataItem(), Entityable {

        abstract val name: String
        abstract val thumbnail: String?
        abstract val count: Long

        @Serializable
        class LocalPlaylist(
            override val name: String,
            override val thumbnail: String?,
            override val count: Long,
            val id: Long,
        ) : Playlist()

        @Serializable
        class OnlinePlaylist(
            override val name: String,
            override val thumbnail: String?,
            override val count: Long,
            val channelName: String,
            val channelUrl: String,
            val isChannelVerified: Boolean,
            val url: String,
        ) : Playlist()

        @Serializable
        class OfflineFirstPlaylist(
            override val name: String,
            override val thumbnail: String?,
            override val count: Long,
            val channelName: String,
            val channelUrl: String,
            val isChannelVerified: Boolean,
            val url: String,
            val id: Long,
        ) : Playlist()
        
        override fun toEntity(): PlaylistEntity = when (this) {
            is LocalPlaylist -> PlaylistEntity(
                name = name,
                count = count,
                thumbnail = thumbnail ?: "",
                id = id,
            )
            
            is OnlinePlaylist -> PlaylistEntity(
                name = name,
                channelUrl = channelUrl,
                channelName = channelName,
                count = count,
                isChannelVerified = isChannelVerified,
                url = url,
                thumbnail = thumbnail ?: "",
            )
            
            is OfflineFirstPlaylist -> PlaylistEntity(
                name = name,
                channelUrl = channelUrl,
                channelName = channelName,
                count = count,
                isChannelVerified = isChannelVerified,
                url = url,
                thumbnail = thumbnail ?: "",
                id = id,
            )
        }
    }
    
    class Channel(
        val url: String,
        val name: String,
        val thumbnail: String?,
        val description: String,
        val subscriberCount: Long,
        val verified: Boolean,
    ) : DataItem(), Entityable {
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
    
    val key: String
        get() = when (this) {
            is Playlist -> "playlist-" + when(this) {
                is Playlist.LocalPlaylist -> id.toString()
                is Playlist.OnlinePlaylist -> url
                is Playlist.OfflineFirstPlaylist -> "$url-$id"
            }
            is Channel -> "channel$url"
            is Video -> "video-$url"
        }
}

enum class StreamType {
    NORMAL, LIVE_STREAM, POST_LIVE_STREAM
}
