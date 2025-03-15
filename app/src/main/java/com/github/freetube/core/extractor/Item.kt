package com.github.freetube.core.extractor

import com.github.freetube.core.extractor.Item.Channel
import com.github.freetube.core.extractor.Item.Comment
import com.github.freetube.core.extractor.Item.Playlist
import com.github.freetube.core.extractor.Item.Video
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.comments.CommentsInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem

sealed class Item(
    val url: String,
    val name: String,
    val thumbnails: List<String>,
) {
    class Video(
        url: String,
        name: String,
        thumbnails: List<String>,
        streamType: StreamType,
        channelName: String,
        shortDescription: String,
        uploadDate: String?,
        viewCount: Long,
        duration: Long,
        channelUrl: String,
        channelVerified: Boolean,
        isShort: Boolean,
        channelAvatars: List<String>,
    ) : Item(url, name, thumbnails)

    class Playlist(
        url: String,
        name: String,
        thumbnails: List<String>,
        channelName: String,
        channelUrl: String,
        channelVerified: Boolean,
    ) : Item(url, name, thumbnails)

    // todo 
    class Comment(
        url: String,
        name: String,
        thumbnails: List<String>,
    ) : Item(url, name, thumbnails)

    class Channel(
        url: String,
        name: String,
        thumbnails: List<String>,
        description: String,
        subscriberCount: Long,
        verified: Boolean,
    ) : Item(url, name, thumbnails)
}

fun List<org.schabi.newpipe.extractor.InfoItem>.toItemList(): List<Item> =
    buildList {
        this@toItemList.forEach {
            when (it) {
                is PlaylistInfoItem ->
                    Playlist(
                        url = it.url,
                        name = it.name,
                        thumbnails = it.thumbnails.map { it.url },
                        channelName = it.uploaderName,
                        channelUrl = it.url,
                        channelVerified = it.isUploaderVerified,
                    )

                is ChannelInfoItem -> Channel(
                    url = it.url,
                    name = it.name,
                    thumbnails = it.thumbnails.map { it.url },
                    description = it.description,
                    subscriberCount = it.subscriberCount,
                    verified = it.isVerified,
                )

                is CommentsInfoItem -> Comment(
                    url = it.url,
                    name = it.name,
                    thumbnails = it.thumbnails.map { it.url },
                )

                is StreamInfoItem -> {
                    val streamType = when (it.streamType) {
                        org.schabi.newpipe.extractor.stream.StreamType.LIVE_STREAM -> StreamType.LIVE_STREAM
                        org.schabi.newpipe.extractor.stream.StreamType.POST_LIVE_STREAM -> StreamType.POST_LIVE_STREAM
                        else -> StreamType.NORMAL
                    }
                    Video(
                        url = it.url,
                        name = it.name,
                        thumbnails = it.thumbnails.map { it.url },
                        streamType = streamType,
                        channelUrl = it.uploaderName,
                        viewCount = it.viewCount,
                        uploadDate = it.textualUploadDate,
                        shortDescription = it.shortDescription,
                        duration = it.duration,
                        channelVerified = it.isUploaderVerified,
                        isShort = it.isShortFormContent,
                        channelAvatars = it.uploaderAvatars.map { it.url },
                        channelName = it.uploaderName,
                    )
                }

                else -> null
            }?.let(::add)
        }
    }
