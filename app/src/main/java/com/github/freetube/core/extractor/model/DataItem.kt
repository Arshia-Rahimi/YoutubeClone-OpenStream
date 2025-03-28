package com.github.freetube.core.extractor.model

import com.github.freetube.core.database.Entityable
import com.github.freetube.core.database.entities.ChannelEntity
import com.github.freetube.core.database.entities.PlaylistEntity
import com.github.freetube.core.database.entities.VideoEntity
import com.github.freetube.core.extractor.model.DataItem.Channel
import com.github.freetube.core.extractor.model.DataItem.Playlist
import com.github.freetube.core.extractor.model.DataItem.Video
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import java.time.format.DateTimeFormatter

sealed class DataItem(
    val url: String?,
    val name: String,
    val thumbnail: String?,
) {
    class Video(
        url: String,
        name: String,
        thumbnail: String?,
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
    ) : DataItem(url, name, thumbnail), Entityable {
        override fun toEntity(parentId: Int?): Any {
            require(parentId != null)
            return VideoEntity(
                playlistId = parentId,
                name = name,
                url = url,
                thumbnail = thumbnail ?: "",
                streamType = streamType,
                uploadDate = uploadDate,
                viewCount = viewCount,
                duration = duration,
                channelUrl = channelUrl,
                channelName = channelName,
                isChannelVerified = channelVerified,
            )
        }
    }

    class Playlist(
        url: String?,
        name: String,
        thumbnail: String?,
        val channelName: String?,
        val channelUrl: String?,
        val channelVerified: Boolean?,
        val count: Long,
    ) : DataItem(url, name, thumbnail), Entityable {
        override fun toEntity(parentId: Int?): Any =
            PlaylistEntity(
                name = name,
                channelUrl = channelUrl,
                channelName = channelName,
                count = count,
                isChannelVerified = channelVerified,
                url = url,
                thumbnail = thumbnail ?: "",
            )
    }

    class Channel(
        url: String,
        name: String,
        thumbnail: String?,
        val description: String,
        val subscriberCount: Long,
        val verified: Boolean,
    ) : DataItem(url, name, thumbnail), Entityable {
        override fun toEntity(parentId: Int?): Any =
            ChannelEntity(
                name = name,
                url = url,
                isVerified = verified,
                subscriberCount = subscriberCount,
                description = description,
                avatar = thumbnail ?: "",
                banner = "",
            )

    }
}

fun List<InfoItem>.toList(): List<DataItem> =
    buildList { this@toList.forEach { it.toDataItem()?.let(::add) } }

private fun InfoItem.toDataItem(): DataItem? =
    when (this) {
        is PlaylistInfoItem ->
            Playlist(
                url = url,
                name = name,
                thumbnail = thumbnails.first().url,
                channelName = uploaderName,
                channelUrl = url,
                channelVerified = isUploaderVerified,
                count = streamCount,
            )

        is ChannelInfoItem ->
            Channel(
                url = url,
                name = name,
                thumbnail = thumbnails.first().url,
                description = description,
                subscriberCount = subscriberCount,
                verified = isVerified,
            )

        is StreamInfoItem ->
            Video(
                url = url,
                name = name,
                thumbnail = thumbnails.first().url,
                channelUrl = uploaderUrl,
                viewCount = viewCount,
                uploadOffset = textualUploadDate ?: "",
                shortDescription = shortDescription,
                duration = duration,
                channelVerified = isUploaderVerified,
                isShort = isShortFormContent,
                channelAvatars = uploaderAvatars.firstOrNull()?.url,
                channelName = uploaderName,
                uploadDate = uploadDate?.offsetDateTime()?.toLocalDateTime()
                    ?.format(DateTimeFormatter.ofPattern("d MMM uuuu")) ?: "",
                streamType = when (streamType) {
                    org.schabi.newpipe.extractor.stream.StreamType.LIVE_STREAM -> StreamType.LIVE_STREAM
                    org.schabi.newpipe.extractor.stream.StreamType.POST_LIVE_STREAM -> StreamType.POST_LIVE_STREAM
                    else -> StreamType.NORMAL
                },
            )

        else -> null
    }
