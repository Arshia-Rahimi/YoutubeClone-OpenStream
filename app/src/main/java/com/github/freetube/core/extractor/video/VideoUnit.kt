package com.github.freetube.core.extractor.video

import androidx.media3.common.MediaItem
import com.github.freetube.core.extractor.YtService
import com.github.freetube.core.extractor.model.StreamType

class VideoUnit(url: String) {
    private val extractor = YtService.getStreamExtractor(url)
    val data: VideoData

    init {
        extractor.fetchPage()
        data = VideoData(
            name = extractor.name,
            url = extractor.url,
            description = extractor.description.content,
            channelUrl = extractor.uploaderUrl,
            channelName = extractor.uploaderName,
            subscriberCount = extractor.uploaderSubscriberCount,
            isChannelVerified = extractor.isUploaderVerified,
            length = extractor.length,
            uploadDate = extractor.textualUploadDate,
            viewCount = extractor.viewCount,
            videoStreams = extractor.videoStreams,
            audioStreams = extractor.audioStreams,
            videoOnlyStreams = extractor.videoOnlyStreams,
            subtitles = extractor.subtitlesDefault,
            likeCount = extractor.likeCount,
            channelAvatar = extractor.uploaderAvatars.first().url,
            streamType = when (extractor.streamType) {
                org.schabi.newpipe.extractor.stream.StreamType.LIVE_STREAM -> StreamType.LIVE_STREAM
                org.schabi.newpipe.extractor.stream.StreamType.POST_LIVE_STREAM -> StreamType.POST_LIVE_STREAM
                else -> StreamType.NORMAL
            },
            mediaItem = MediaItem.fromUri(extractor.url),
        )
    }
}
