package com.github.openstream.core.model.extractordata

import com.github.openstream.core.model.dataitem.StreamType
import com.github.openstream.core.model.dataitem.VideoItem

data class VideoData(
    val name: String,
    val url: String,
    val description: String,
    val channelUrl: String,
    val channelName: String,
    val viewCount: Long,
    val uploadDate: Long?,
    val duration: Long,
    val likeCount: Long,
    val isChannelVerified: Boolean,
    val channelAvatar: String,
    val subscriberCount: Long,
    val streamType: StreamType,
    // todo get url and quality from streams
    val audioStream: String,
    val videoOptions: List<VideoOption>,
//    val subtitles: List<SubtitlesStream>,
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
        duration = duration,
        shortDescription = description,
        viewCount = viewCount,
        uploadDate = uploadDate,
    )

}
   
data class VideoOption(
    val content: String,
    val quality: String,
)
