package com.github.freetube.ui.designsystem.dataitem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.arshia.freetube.R
import com.github.freetube.core.common.toViewCount
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.ui.designsystem.components.noRippleClickable

@Composable
fun Channel(
    item: DataItem.Channel,
    toChannelScreen: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .noRippleClickable { toChannelScreen(item.url) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.4f)
                .padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .aspectRatio(1f),
                model = item.thumbnail,
                contentDescription = "channel avatar",
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.6f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.name,
                    fontSize = 20.sp,
                    color = Color.White,
                )
                if (item.verified) {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        painter = painterResource(R.drawable.verified),
                        contentDescription = "verified",
                        tint = Color(0xFFAAAAAA)
                    )
                }
            }
            Text(
                text = "${item.subscriberCount.toViewCount()} subscribers",
                color = Color(0xFFAAAAAA)
            )
        }
        Button(
            onClick = {
                // todo
            },
            modifier = Modifier.padding(end = 16.dp, start = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFCC2849),
            )
        ) {
            Text(
                text = "Subscribe",
                color = Color.White,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        Channel(
            DataItem.Channel(
                url = "",
                name = "channel name",
                description = "description",
                verified = true,
                subscriberCount = 45552365L,
                thumbnail = "",
            ),
            toChannelScreen = {},
        )
    }
}
