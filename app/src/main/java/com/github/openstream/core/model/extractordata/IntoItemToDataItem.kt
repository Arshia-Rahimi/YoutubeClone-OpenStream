package com.github.openstream.core.model.extractordata

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.buildImmutableArray
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import java.time.format.DateTimeFormatter
import kotlin.collections.forEach

fun List<InfoItem>.toImmutableArrayOfDataItem(): ImmutableArray<DataItem> =
    buildImmutableArray { this@toImmutableArrayOfDataItem.forEach { it.toDataItem()?.let(::add) } }

fun List<InfoItem>.toListOfDataItem(): List<DataItem> =
    buildList { this@toListOfDataItem.forEach { it.toDataItem()?.let(::add) } }

fun List<InfoItem>.toMutableStateListOfDataItem(): SnapshotStateList<DataItem> =
    toListOfDataItem().toMutableStateList()

private fun InfoItem.toDataItem(): DataItem? = when (this) {
    is PlaylistInfoItem -> DataItem.Playlist.OnlinePlaylist(
        url = url,
        name = name,
        thumbnail = thumbnails.first().url,
        channelName = uploaderName,
        channelUrl = url,
        isChannelVerified = isUploaderVerified,
        count = streamCount,
        channelAvatar = null,
    )
    
    is ChannelInfoItem -> DataItem.Channel(
        url = url,
        name = name,
        thumbnail = thumbnails.first().url,
        description = description,
        subscriberCount = subscriberCount,
        verified = isVerified,
    )
    
    is StreamInfoItem -> DataItem.Video(
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

