package com.github.arshiarahimi.openstream.core.model.dataitem

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
            .filter { it is StreamInfoItem }
            .forEach {
                it.toDataItem()?.let { add(it as VideoItem) }
            }
    }

private fun InfoItem.toDataItem(): DataItem? = when (this) {
    is PlaylistInfoItem -> PlaylistItem.OnlinePlaylistItem(
        url = url,
        name = name,
        thumbnail = thumbnails.first().url,
        channelName = uploaderName ?: "",
        channelUrl = url,
        isChannelVerified = isUploaderVerified,
        count = streamCount,
    )

    is ChannelInfoItem -> ChannelItem.OnlineChannelItem(
        url = url,
        name = name,
        avatar = thumbnails.first().url,
        description = description,
        subscriberCount = subscriberCount,
        isVerified = isVerified,
    )

    is StreamInfoItem -> VideoItem(
        url = url,
        name = name,
        thumbnail = thumbnails.first().url,
        channelUrl = uploaderUrl,
        viewCount = viewCount,
        shortDescription = shortDescription,
        duration = duration,
        isChannelVerified = isUploaderVerified,
        isShort = isShortFormContent,
        channelAvatars = uploaderAvatars.firstOrNull()?.url,
        channelName = uploaderName,
        uploadDate = uploadDate?.offsetDateTime()?.toInstant()?.toEpochMilli(),
        streamType = when (streamType) {
            StreamType.LIVE_STREAM -> com.github.arshiarahimi.openstream.core.model.dataitem.StreamType.LIVE_STREAM
            StreamType.POST_LIVE_STREAM -> com.github.arshiarahimi.openstream.core.model.dataitem.StreamType.POST_LIVE_STREAM
            else -> com.github.arshiarahimi.openstream.core.model.dataitem.StreamType.NORMAL
        },
    )

    else -> null
}
