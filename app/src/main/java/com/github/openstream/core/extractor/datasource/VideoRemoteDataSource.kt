package com.github.openstream.core.extractor.datasource

import com.github.openstream.core.extractor.YtService
import com.github.openstream.core.model.extractordata.VideoData
import org.schabi.newpipe.extractor.stream.StreamType

object VideoRemoteDataSource {
    fun fetchVideo(url: String): VideoData {
        val extractor = YtService.getStreamExtractor(url)
        extractor.fetchPage()
        return VideoData(
            name = extractor.name,
            url = extractor.url,
            description = extractor.description.content,
            channelUrl = extractor.uploaderUrl,
            channelName = extractor.uploaderName,
            subscriberCount = extractor.uploaderSubscriberCount,
            isChannelVerified = extractor.isUploaderVerified,
            duration = extractor.length,
            uploadDate = extractor.uploadDate?.offsetDateTime()?.toInstant()?.toEpochMilli(),
            viewCount = extractor.viewCount,
            videoStreams = extractor.videoStreams,
            audioStreams = extractor.audioStreams,
            videoOnlyStreams = extractor.videoOnlyStreams,
            subtitles = extractor.subtitlesDefault,
            likeCount = extractor.likeCount,
            channelAvatar = extractor.uploaderAvatars.first().url,
            streamType = when (extractor.streamType) {
                StreamType.LIVE_STREAM -> com.github.openstream.core.model.dataitem.StreamType.LIVE_STREAM
                StreamType.POST_LIVE_STREAM -> com.github.openstream.core.model.dataitem.StreamType.POST_LIVE_STREAM
                else -> com.github.openstream.core.model.dataitem.StreamType.NORMAL
            },
        )
    }
}
