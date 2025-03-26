package com.github.freetube.ui.global.player.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.freetube.core.extractor.model.StreamType
import com.github.freetube.core.extractor.video.VideoData

@Composable
fun ColumnScope.SheetBody(
    videoData: VideoData,
) {
    
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        Column(Modifier.fillMaxSize()) {
            SheetBody(
                VideoData(
                    name = "name",
                    description = "description",
                    channelAvatar = "",
                    isChannelVerified = true,
                    length = 34324L,
                    uploadDate = "5",
                    viewCount = 454222L,
                    channelUrl = "",
                    channelName = "channel",
                    url = "",
                    videoStreams = emptyList(),
                    audioStreams = emptyList(),
                    videoOnlyStreams = emptyList(),
                    streamType = StreamType.NORMAL,
                    subtitles = emptyList(),
                    subscriberCount = 454321L,
                    likeCount = 3234L,
                )
            )
        }
    }
}
