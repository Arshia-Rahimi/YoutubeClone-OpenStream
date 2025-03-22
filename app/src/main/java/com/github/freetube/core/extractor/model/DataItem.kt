package com.github.freetube.core.extractor.model

import com.github.freetube.core.extractor.model.DataItem.Channel
import com.github.freetube.core.extractor.model.DataItem.Comment
import com.github.freetube.core.extractor.model.DataItem.Playlist
import com.github.freetube.core.extractor.model.DataItem.Video
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.comments.CommentsInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem

sealed class DataItem(
    val url: String,
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
        val uploadDate: String?,
        val viewCount: Long,
        val duration: Long,
        val channelUrl: String,
        val channelVerified: Boolean,
        val isShort: Boolean,
        val channelAvatars: String?,
    ) : DataItem(url, name, thumbnail)

    class Playlist(
        url: String,
        name: String,
        thumbnail: String?,
        val channelName: String,
        val channelUrl: String,
        val channelVerified: Boolean,
    ) : DataItem(url, name, thumbnail)

    // todo 
    class Comment(
        url: String,
        name: String,
        thumbnail: String?,
    ) : DataItem(url, name, thumbnail)

    class Channel(
        url: String,
        name: String,
        thumbnail: String?,
        val description: String,
        val subscriberCount: Long,
        val verified: Boolean,
    ) : DataItem(url, name, thumbnail)
}

fun List<InfoItem>.toList(): List<DataItem> = buildList { this@toList.forEach { it.toDataItem()?.let(::add) } }

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

        is CommentsInfoItem ->
            Comment(
                url = url,
                name = name,
                thumbnail = thumbnails.first().url,
            )

        is StreamInfoItem ->
            Video(
                url = url,
                name = name,
                thumbnail = thumbnails.first().url,
                channelUrl = uploaderUrl,
                viewCount = viewCount,
                uploadDate = textualUploadDate,
                shortDescription = shortDescription,
                duration = duration,
                channelVerified = isUploaderVerified,
                isShort = isShortFormContent,
                channelAvatars = uploaderAvatars.firstOrNull()?.url,
                channelName = uploaderName,
                streamType = when (streamType) {
                    org.schabi.newpipe.extractor.stream.StreamType.LIVE_STREAM -> StreamType.LIVE_STREAM
                    org.schabi.newpipe.extractor.stream.StreamType.POST_LIVE_STREAM -> StreamType.POST_LIVE_STREAM
                    else -> StreamType.NORMAL
                },
            )

        else -> null
    }
