package com.github.openstream.core.model.extractordata

import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem

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
        channelName = uploaderName,
        channelUrl = url,
        isChannelVerified = isUploaderVerified,
        count = streamCount,
    )

    is ChannelInfoItem -> ChannelItem(
        url = url,
        name = name,
        thumbnail = thumbnails.first().url,
        description = description,
        subscriberCount = subscriberCount,
        verified = isVerified,
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
            org.schabi.newpipe.extractor.stream.StreamType.LIVE_STREAM -> StreamType.LIVE_STREAM
            org.schabi.newpipe.extractor.stream.StreamType.POST_LIVE_STREAM -> StreamType.POST_LIVE_STREAM
            else -> StreamType.NORMAL
        },
    )

    else -> null
}

