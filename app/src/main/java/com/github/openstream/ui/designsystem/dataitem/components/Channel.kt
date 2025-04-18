package com.github.openstream.ui.designsystem.dataitem.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import com.arshia.openstream.R
import com.github.openstream.core.common.util.toShortForm
import com.github.openstream.core.extractor.model.DataItem

@Composable
fun Channel(
    modifier: Modifier,
    item: DataItem.Channel,
    toChannelScreen: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { toChannelScreen(item.url ?: "") }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.4f)
                .padding(end = 4.dp)
                .padding(vertical = 8.dp),
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
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
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
                text = "${item.subscriberCount.toShortForm()} subscribers",
                color = MaterialTheme.colorScheme.onTertiary,
                maxLines = 1,
                fontSize = 12.sp,
            )
        }
        Button(
            onClick = {
                // todo
            },
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
            item = DataItem.Channel(
                url = "",
                name = "channel name",
                description = "description",
                verified = true,
                subscriberCount = 45552365L,
                thumbnail = "",
            ),
            toChannelScreen = {},
            modifier = Modifier,
        )
    }
}
