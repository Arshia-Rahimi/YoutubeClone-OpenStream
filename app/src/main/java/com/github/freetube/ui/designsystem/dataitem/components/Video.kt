package com.github.freetube.ui.designsystem.dataitem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.arshia.freetube.R
import com.github.freetube.core.common.toViewCount
import com.github.freetube.core.common.util.toTime
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.model.StreamType
import com.github.freetube.ui.designsystem.components.noRippleClickable

@Composable
fun Video(
    item: DataItem.Video,
    toChannelScreen: (String) -> Unit,
    playVideo: (String) -> Unit,
) {
    var isDropDownExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f)
                .clip(RoundedCornerShape(24.dp))
                .noRippleClickable { playVideo(item.url) },
        ) {
            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = item.thumbnail,
                contentDescription = "thumbnail",
                contentScale = ContentScale.FillHeight,
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                text = item.duration.toTime(),
                color = Color.White,
                fontSize = 16.sp,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f),
            verticalAlignment = Alignment.Top,
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clip(CircleShape)
                    .width(40.dp)
                    .aspectRatio(1f)
                    .noRippleClickable { toChannelScreen(item.channelUrl) },
                model = item.channelAvatars,
                contentDescription = "channel avatar",
            )
            Column(
                modifier = Modifier.weight(1f),
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
                        .fillMaxWidth()
                        .weight(1f)
                        .noRippleClickable { playVideo(item.url) },
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SubText(text = item.channelName)
                    if (item.channelVerified) {
                        Icon(
                            modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                            painter = painterResource(R.drawable.verified),
                            contentDescription = "verified",
                            tint = Color(0xFFAAAAAA)
                        )
                    }
                    SubText(
                        text = " • ${item.viewCount.toViewCount()} • ${item.uploadDate}"
                    )
                }
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
                ) {
                    DropdownMenuItem(
                        text = { Text("test") },
                        onClick = {}
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
        color = Color(0xFFAAAAAA)
    )
}

@Preview
@Composable
private fun Preview(
    item: DataItem.Video = DataItem.Video(
        url = "",
        name = "name",
        thumbnail = "",
        streamType = StreamType.NORMAL,
        channelName = "channelName",
        channelAvatars = "",
        shortDescription = "description",
        uploadDate = "5 days ago",
        viewCount = 4566604L,
        duration = 120000L,
        channelUrl = "",
        channelVerified = true,
        isShort = false,
    )
) {
    MaterialTheme {
        Video(item = item, {}, {})
    }
}
