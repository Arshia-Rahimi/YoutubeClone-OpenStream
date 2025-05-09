package com.github.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.PlaylistVideoCrossRef
import com.github.openstream.core.database.entities.VideoEntity
import com.github.openstream.core.model.LocalOnlyPlaylist
import com.github.openstream.core.model.OfflineFirstPlaylist
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import com.github.openstream.core.model.extractordata.VideoItem

data class PlaylistWithVideos(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "videoId",
        associateBy = Junction(PlaylistVideoCrossRef::class),
    )
    val videos: List<VideoEntity>,
) {
    fun toPlaylistObject(): Playlist =
        when {
            playlist.url == null -> LocalOnlyPlaylist(
                id = playlist.playlistId,
                items = videos.map { video ->
                    VideoItem(
                        url = video.url,
                        name = video.name,
                        thumbnail = video.thumbnail,
                        streamType = video.streamType,
                        channelUrl = video.channelUrl ?: "",
                        channelName = video.channelName,
                        shortDescription = "",
                        uploadDate = video.uploadDate,
                        viewCount = video.viewCount,
                        isShort = false,
                        duration = video.duration,
                        channelAvatars = "",
                        channelVerified = video.isChannelVerified,
                        id = playlist.playlistId,
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
                id = playlist.playlistId,
                items = videos.map { video ->
                    VideoItem(
                        url = video.url,
                        name = video.name,
                        thumbnail = video.thumbnail,
                        streamType = video.streamType,
                        channelUrl = video.channelUrl ?: "",
                        channelName = video.channelName,
                        shortDescription = "",
                        uploadDate = video.uploadDate,
                        viewCount = video.viewCount,
                        isShort = false,
                        duration = video.duration,
                        channelAvatars = "",
                        channelVerified = video.isChannelVerified,
                        id = playlist.playlistId,
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
