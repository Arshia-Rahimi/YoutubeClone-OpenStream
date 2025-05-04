package com.github.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.VideoEntity
import com.github.openstream.core.model.LocalPlaylist
import com.github.openstream.core.model.OfflineFirstPlaylist
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata

data class PlaylistWithVideos(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "id",
    )
    val videos: List<VideoEntity>,
) {
    fun toPlaylistObject(): Playlist =
        when {
            playlist.url == null -> LocalPlaylist(
                id = playlist.id,
                items = videos.map { video ->
                    DataItem.Video(
                        url = video.url,
                        name = video.name,
                        thumbnail = video.thumbnail,
                        streamType = video.streamType,
                        channelUrl = video.channelUrl ?: "",
                        channelName = video.channelName,
                        shortDescription = "",
                        uploadDate = video.uploadDate,
                        uploadOffset = video.uploadDate,
                        viewCount = video.viewCount,
                        isShort = false,
                        duration = video.duration,
                        channelAvatars = "",
                        channelVerified = video.isChannelVerified,
                    )
                }.toTypedArray(),
                metadata = PlaylistMetadata(
                    name = playlist.name,
                    channelUrl = playlist.channelUrl,
                    isChannelVerified = playlist.isChannelVerified,
                    count = playlist.count,
                    channelName = playlist.channelName,
                )
            )

            else -> OfflineFirstPlaylist(
                id = playlist.id,
                items = videos.map { video ->
                    DataItem.Video(
                        url = video.url,
                        name = video.name,
                        thumbnail = video.thumbnail,
                        streamType = video.streamType,
                        channelUrl = video.channelUrl ?: "",
                        channelName = video.channelName,
                        shortDescription = "",
                        uploadDate = video.uploadDate,
                        uploadOffset = video.uploadDate,
                        viewCount = video.viewCount,
                        isShort = false,
                        duration = video.duration,
                        channelAvatars = "",
                        channelVerified = video.isChannelVerified,
                    )
                }.toTypedArray(),
                metadata = PlaylistMetadata(
                    name = playlist.name,
                    channelUrl = playlist.channelUrl,
                    isChannelVerified = playlist.isChannelVerified,
                    count = playlist.count,
                    channelName = playlist.channelName,
                ),
                url = playlist.url,
            )
        }
}