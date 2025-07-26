package com.github.openstream.core.shared.extractor.data

import com.github.openstream.core.shared.StreamType
import com.github.openstream.core.shared.dataitem.VideoItem

data class VideoData(
    val thumbnail: String?,
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
    val audioStream: String,
    val videoOptions: List<VideoOption>,
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
    val quality: VideoQuality
)

enum class VideoQuality(
    val quality: Int,
) {
    Q144p(144),
    Q240p(240),
    Q360p(360),
    Q480p(480),
    Q720p(720),
    Q1080p(1080),
}
