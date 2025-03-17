package com.github.freetube.ui.designsystem.dataitems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.arshia.freetube.R
import com.github.freetube.core.common.util.toTime
import com.github.freetube.core.extractor.DataItem
import com.github.freetube.core.extractor.StreamType
import com.github.freetube.ui.designsystem.components.noRippleClickable

@Composable
fun Video(
    modifier: Modifier,
    item: DataItem.Video,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.25f)),
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                    text = item.duration.toTime(),
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
            AsyncImage(
                modifier = Modifier
                    .matchParentSize(),
                contentScale = ContentScale.FillBounds,
                model = item.url,
                contentDescription = "thumbnail",
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                AsyncImage(
                    modifier = Modifier
                        .noRippleClickable {
                            // todo open channel page
                        },
                    model = item.thumbnails.first(),
                    contentDescription = "channel avatar",
                )
            }
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = item.name,
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    color = Color.White,
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
                        text = "• ${item.viewCount} • ${item.uploadDate}"
                    )
                }
            }
            Button(
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
    modifier: Modifier = Modifier,
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
        viewCount = 45666L,
        duration = 120000L,
        channelUrl = "",
        channelVerified = true,
        isShort = false,
    )
) {
    MaterialTheme {
        Video(item = item, modifier = Modifier)
    }
}
