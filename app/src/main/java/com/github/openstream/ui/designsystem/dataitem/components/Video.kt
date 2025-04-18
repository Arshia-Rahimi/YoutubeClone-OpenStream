package com.github.openstream.ui.designsystem.dataitem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.arshia.openstream.R
import com.github.openstream.core.common.util.toShortForm
import com.github.openstream.core.common.util.toTime
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.model.StreamType

@Composable
fun Video(
    modifier: Modifier,
    item: DataItem.Video,
    toChannelScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
    shouldViewChannel: Boolean,
) {
    val timeString = "${item.viewCount.toShortForm()} views • " + when (item.streamType) {
        StreamType.NORMAL -> ""
        StreamType.LIVE_STREAM -> "started streaming"
        StreamType.POST_LIVE_STREAM -> "streamed "
    } + item.uploadOffset
    var isDropDownExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { playVideo(item.url ?: "") }
            .clip(RoundedCornerShape(12.dp)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.4f)
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(16.dp)),
        ) {
            AsyncImage(
                model = item.thumbnail,
                contentDescription = "thumbnail",
                modifier = Modifier.matchParentSize(),
            )
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 4.dp)
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(0.5f))
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                    text = item.duration.toTime(),
                    fontSize = 12.sp,
                    color = Color.White,
                )
            }
        }
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = item.name,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SubText(text = item.channelName)
                if (item.channelVerified) {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        painter = painterResource(R.drawable.verified),
                        contentDescription = "verified",
                        tint = Color(0xFFAAAAAA)
                    )
                }
            }
            SubText(
                text = timeString
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Top)
                .width(24.dp)
        ) {
            IconButton(
                modifier = Modifier,
                onClick = { isDropDownExpanded = !isDropDownExpanded },
            ) {
                Icon(
                    painter = painterResource(R.drawable.options),
                    contentDescription = "options",
                    tint = Color.White,
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false },
                tonalElevation = 4.dp,
            ) {
                if (shouldViewChannel) {
                    DropdownMenuItem(
                        text = { Text("view channel") },
                        onClick = {
                            isDropDownExpanded = false
                            toChannelScreen(item.channelUrl)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun SubText(
    text: String,
) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = Color(0xFFAAAAAA),
        maxLines = 1,
    )
}


@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        Video(
            item = DataItem.Video(
                name = "name",
                channelUrl = "",
                isShort = false,
                channelName = "channel",
                viewCount = 545664L,
                channelVerified = true,
                shortDescription = "description",
                streamType = StreamType.NORMAL,
                channelAvatars = "",
                url = "",
                thumbnail = "",
                uploadDate = "5 days ago",
                duration = 14533L,
                uploadOffset = "",
            ),
            toChannelScreen = {},
            playVideo = {},
            shouldViewChannel = true,
            modifier = Modifier,
        )
    }
}
