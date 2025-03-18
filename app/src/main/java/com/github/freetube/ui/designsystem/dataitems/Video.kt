package com.github.freetube.ui.designsystem.dataitems

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.github.freetube.core.extractor.DataItem
import com.github.freetube.core.extractor.StreamType
import com.github.freetube.ui.designsystem.components.noRippleClickable

@Composable
fun Video(
    item: DataItem.Video,
) {
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
                .clip(RoundedCornerShape(24.dp)),
        ) {
            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = item.thumbnails.first(),
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
                    .noRippleClickable {
                        // todo open channel page
                    },
                model = item.channelAvatars.first(),
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
                        .weight(1f),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SubText(text = item.channelName)
                    if (item.channelVerified) {
                        Icon(
                            modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                            painter = painterResource(R.drawable.verified),
                            contentDescription = "verified",
                        )
                    }
                    SubText(
                        text = " • ${item.viewCount.toViewCount()} • ${item.uploadDate}"
                    )
                }
            }
            // todo is not shown
            Button(
                modifier = Modifier.width(16.dp),
                onClick = {
                    // todo open options
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.options),
                    contentDescription = "options",
                    tint = Color.White,
                )
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
        thumbnails = listOf(""),
        streamType = StreamType.NORMAL,
        channelName = "channelName",
        channelAvatars = listOf(""),
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
        Video(item = item)
    }
}
