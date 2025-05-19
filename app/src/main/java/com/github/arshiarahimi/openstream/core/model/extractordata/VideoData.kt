package com.github.arshiarahimi.openstream.core.model.extractordata

import com.github.arshiarahimi.openstream.core.model.dataitem.StreamType
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import org.schabi.newpipe.extractor.stream.AudioStream
import org.schabi.newpipe.extractor.stream.SubtitlesStream
import org.schabi.newpipe.extractor.stream.VideoStream

data class VideoData(
    val name: String,
    val url: String,
    val description: String,
    val channelUrl: String,
    val channelName: String,
    val viewCount: Long,
    val uploadDate: Long?,
    val length: Long,
    val likeCount: Long,
    val isChannelVerified: Boolean,
    val channelAvatar: String,
    val subscriberCount: Long,
    val streamType: StreamType,
    val audioStreams: List<AudioStream>,
    val videoStreams: List<VideoStream>,
    val videoOnlyStreams: List<VideoStream>,
    val subtitles: List<SubtitlesStream>,
    val id: Long? = null,
) {
    fun toDataItem() = VideoItem(
        id = id,
        name = name,
        thumbnail = null,
        url = url,
        streamType = streamType,
        channelName = channelName,
        channelAvatars = channelAvatar,
        isShort = false,
        isChannelVerified = this@VideoData.isChannelVerified,
        channelUrl = channelUrl,
        duration = length,
        shortDescription = description,
        viewCount = viewCount,
        uploadDate = uploadDate,
    )
}
