package com.github.freetube.core.extractor

import com.github.freetube.core.extractor.DataItem.Channel
import com.github.freetube.core.extractor.DataItem.Comment
import com.github.freetube.core.extractor.DataItem.Playlist
import com.github.freetube.core.extractor.DataItem.Video
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.comments.CommentsInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem

sealed class DataItem(
    val url: String,
    val name: String,
    val thumbnails: List<String>,
) {
    class Video(
        url: String,
        name: String,
        thumbnails: List<String>,
        val streamType: StreamType,
        val channelName: String,
        val shortDescription: String?,
        val uploadDate: String?,
        val viewCount: Long,
        val duration: Long,
        val channelUrl: String,
        val channelVerified: Boolean,
        val isShort: Boolean,
        val channelAvatars: List<String>,
    ) : DataItem(url, name, thumbnails)

    class Playlist(
        url: String,
        name: String,
        thumbnails: List<String>,
        val channelName: String,
        val channelUrl: String,
        val channelVerified: Boolean,
    ) : DataItem(url, name, thumbnails)

    // todo 
    class Comment(
        url: String,
        name: String,
        thumbnails: List<String>,
    ) : DataItem(url, name, thumbnails)

    class Channel(
        url: String,
        name: String,
        thumbnails: List<String>,
        val description: String,
        val subscriberCount: Long,
        val verified: Boolean,
    ) : DataItem(url, name, thumbnails)
}

fun List<InfoItem>.toList(): List<DataItem> = buildList { this@toList.forEach { it.toDataItem()?.let(::add) } }

private fun InfoItem.toDataItem(): DataItem? =
    when (this) {
        is PlaylistInfoItem ->
            Playlist(
                url = url,
                name = name,
                thumbnails = thumbnails.map { it.url },
                channelName = uploaderName,
                channelUrl = url,
                channelVerified = isUploaderVerified,
            )

        is ChannelInfoItem ->
            Channel(
                url = url,
                name = name,
                thumbnails = thumbnails.map { it.url },
                description = description,
                subscriberCount = subscriberCount,
                verified = isVerified,
            )

        is CommentsInfoItem ->
            Comment(
                url = url,
                name = name,
                thumbnails = thumbnails.map { it.url },
            )

        is StreamInfoItem ->
            Video(
                url = url,
                name = name,
                thumbnails = thumbnails.map { it.url },
                channelUrl = uploaderName,
                viewCount = viewCount,
                uploadDate = textualUploadDate,
                shortDescription = shortDescription,
                duration = duration,
                channelVerified = isUploaderVerified,
                isShort = isShortFormContent,
                channelAvatars = uploaderAvatars.map { it.url },
                channelName = uploaderName,
                streamType = when (streamType) {
                    org.schabi.newpipe.extractor.stream.StreamType.LIVE_STREAM -> StreamType.LIVE_STREAM
                    org.schabi.newpipe.extractor.stream.StreamType.POST_LIVE_STREAM -> StreamType.POST_LIVE_STREAM
                    else -> StreamType.NORMAL
                },
            )

        else -> null
    }
