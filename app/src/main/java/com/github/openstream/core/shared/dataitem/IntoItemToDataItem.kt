package com.github.openstream.core.shared.dataitem

import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import org.schabi.newpipe.extractor.stream.StreamType

fun List<InfoItem>.toListOfDataItem(): List<DataItem> =
    buildList { this@toListOfDataItem.forEach { it.toDataItem()?.let(::add) } }

fun List<InfoItem>.toListOfVideos(): List<VideoItem> =
    buildList {
        this@toListOfVideos
            .filterIsInstance<StreamInfoItem>()
            .forEach { video ->
                video.toDataItem()?.let { add(it as VideoItem) }
            }
    }

private fun InfoItem.toDataItem(): DataItem? = when (this) {
    is PlaylistInfoItem -> PlaylistItem.OnlinePlaylistItem(
        url = url,
        name = name,
        thumbnail = thumbnails.last().url,
        channelName = uploaderName ?: "",
        channelUrl = url,
        isChannelVerified = isUploaderVerified,
        count = streamCount,
    )

    is ChannelInfoItem -> ChannelItem.OnlineChannelItem(
        url = url,
        name = name,
        avatar = thumbnails.last().url,
        description = description ?: "",
        subscriberCount = subscriberCount,
        isVerified = isVerified,
    )

    is StreamInfoItem -> VideoItem(
        url = url,
        name = name,
        thumbnail = thumbnails.last().url,
        channelUrl = uploaderUrl,
        viewCount = viewCount,
        shortDescription = shortDescription ?: "",
        duration = duration,
        isChannelVerified = isUploaderVerified,
        isShort = isShortFormContent,
        channelAvatars = uploaderAvatars.lastOrNull()?.url,
        channelName = uploaderName,
        uploadDate = uploadDate?.offsetDateTime()?.toInstant()?.toEpochMilli(),
        streamType = when (streamType) {
            StreamType.LIVE_STREAM -> com.github.openstream.core.shared.StreamType.LIVE_STREAM
            StreamType.POST_LIVE_STREAM -> com.github.openstream.core.shared.StreamType.POST_LIVE_STREAM
            else -> com.github.openstream.core.shared.StreamType.NORMAL
        },
    )

    else -> null
}
