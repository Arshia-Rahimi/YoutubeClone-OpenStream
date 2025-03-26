package com.github.freetube.ui.global.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arshia.freetube.R
import com.github.freetube.core.common.toShortForm
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.model.StreamType
import com.github.freetube.core.extractor.video.VideoData
import com.github.freetube.ui.designsystem.dataitem.components.Channel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetBody(
    videoData: VideoData,
    toChannelScreen: (String) -> Unit,
) {
    var showDescription by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111111))
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .clickable {
                    scope.launch { showDescription = true }
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = videoData.name,
                fontSize = 20.sp,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(if (!showDescription) R.drawable.condense else R.drawable.expand),
                contentDescription = "description sheet",
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.view),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiary,
            )
            Text(
                text = videoData.viewCount.toShortForm(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onTertiary,
            )
            Icon(
                painter = painterResource(R.drawable.calendar),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiary,
            )
            Text(
                text = videoData.uploadDate,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onTertiary,
            )
        }
        Channel(
            toChannelScreen = toChannelScreen,
            item = DataItem.Channel(
                url = videoData.channelUrl,
                thumbnail = videoData.channelAvatar,
                name = videoData.channelName,
                verified = videoData.isChannelVerified,
                subscriberCount = videoData.subscriberCount,
                description = "",
            )
        )
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        Column(Modifier.fillMaxSize()) {
            SheetBody(
                toChannelScreen = {},
                videoData = VideoData(
                    name = "name",
                    description = "description",
                    channelAvatar = "",
                    isChannelVerified = true,
                    length = 34324L,
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
                    uploadDate = "",
                )
            )
        }
    }
}
