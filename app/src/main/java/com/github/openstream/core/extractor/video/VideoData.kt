package com.github.openstream.core.extractor.video

import com.github.openstream.core.extractor.model.StreamType
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
    val uploadDate: String,
    val length: Long,
    val likeCount: Long,
    val isChannelVerified: Boolean,
    val channelAvatar: String?,
    val subscriberCount: Long,
    val streamType: StreamType,
    val audioStreams: List<AudioStream>,
    val videoStreams: List<VideoStream>,
    val videoOnlyStreams: List<VideoStream>,
    val subtitles: List<SubtitlesStream>,
)
