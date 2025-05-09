package com.github.openstream.core.extractor

import androidx.media3.common.MediaItem
import com.github.openstream.core.extractor.util.YtService
import com.github.openstream.core.model.extractordata.VideoData
import org.schabi.newpipe.extractor.stream.StreamType

object VideoExtractor {
    
    fun fetchVideo(url: String): MediaItem {
        val extractor = YtService.getStreamExtractor(url)
        extractor.fetchPage()
        val data = VideoData(
            name = extractor.name,
            url = extractor.url,
            description = extractor.description.content,
            channelUrl = extractor.uploaderUrl,
            channelName = extractor.uploaderName,
            subscriberCount = extractor.uploaderSubscriberCount,
            isChannelVerified = extractor.isUploaderVerified,
            length = extractor.length,
            uploadDate = extractor.uploadDate?.offsetDateTime()?.toInstant()?.toEpochMilli(),
            viewCount = extractor.viewCount,
            videoStreams = extractor.videoStreams,
            audioStreams = extractor.audioStreams,
            videoOnlyStreams = extractor.videoOnlyStreams,
            subtitles = extractor.subtitlesDefault,
            likeCount = extractor.likeCount,
            channelAvatar = extractor.uploaderAvatars.first().url,
            streamType = when (extractor.streamType) {
                StreamType.LIVE_STREAM -> com.github.openstream.core.model.extractordata.StreamType.LIVE_STREAM
                StreamType.POST_LIVE_STREAM -> com.github.openstream.core.model.extractordata.StreamType.POST_LIVE_STREAM
                else -> com.github.openstream.core.model.extractordata.StreamType.NORMAL
            },
        )
        val url = data.videoStreams.first().content
        return MediaItem.Builder()
            .setTag(data)
            .setUri(url)
            .build()
    }
}
