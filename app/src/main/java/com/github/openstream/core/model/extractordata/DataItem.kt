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
        val playlistId: Int? = null,
        val channelId: Int? = null,
    ) : DataItem(), Entityable {
        override fun toEntity(): VideoEntity {
            return VideoEntity(
                playlistId = playlistId,
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
                channelId = channelId,
            )
        }
    }
    
    @Serializable
    sealed class Playlist(
        val name: String,
        val thumbnail: String?,
        val count: Long,
    ) : DataItem(), Entityable {
        
        class LocalPlaylist(
            name: String,
            thumbnail: String?,
            count: Long,
            val id: Int,
        ) : Playlist(
            name = name,
            thumbnail = thumbnail,
            count = count,
        )
        
        class OnlinePlaylist(
            name: String,
            thumbnail: String?,
            count: Long,
            val channelName: String,
            val channelUrl: String,
            val isChannelVerified: Boolean,
            val url: String,
        ) : Playlist(
            name = name,
            thumbnail = thumbnail,
            count = count,
        )
        
        class OfflineFirstPlaylist(
            name: String,
            thumbnail: String?,
            count: Long,
            val channelName: String,
            val channelUrl: String,
            val isChannelVerified: Boolean,
            val url: String,
            val id: Int,
        ) : Playlist(
            name = name,
            thumbnail = thumbnail,
            count = count,
        )
        
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
            is Playlist -> when(this) {
                is Playlist.LocalPlaylist -> id.toString()
                is Playlist.OnlinePlaylist -> url
                is Playlist.OfflineFirstPlaylist -> url
            }
            is Channel -> url
            is Video -> url
        } + UUID.randomUUID()
}

enum class StreamType {
    NORMAL, LIVE_STREAM, POST_LIVE_STREAM
}
