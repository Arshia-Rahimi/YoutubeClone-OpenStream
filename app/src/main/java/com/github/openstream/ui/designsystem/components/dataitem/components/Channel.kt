package com.github.openstream.ui.designsystem.components.dataitem.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.util.toShortForm
import com.github.openstream.core.shared.dataitem.ChannelItem

@Composable
fun Channel(
    modifier: Modifier,
    item: ChannelItem,
    toChannelScreen: (String) -> Unit,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit,
    unsubscribe: (ChannelItem.OfflineFirstChannelItem) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { toChannelScreen(item.url) }
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .aspectRatio(1f),
                model = item.avatar,
                contentDescription = "channel avatar",
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
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
                    fontSize = 12.sp,
                    color = Color.White,
                    maxLines = 1,
                )
                if (item.isVerified) {
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
                fontSize = 10.sp,
            )
        }
        when (item) {
            is ChannelItem.OnlineChannelItem -> {
                Button(
                    onClick = { subscribe(item) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFCC2849),
                    )
                ) {
                    Text(
                        text = stringResource(R.string.subscribe),
                        color = Color.White,
                        fontSize = 12.sp,
                    )
                }
            }

            is ChannelItem.OfflineFirstChannelItem -> {
                Button(
                    onClick = { unsubscribe(item) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF242424),
                    )
                ) {
                    Text(
                        text = stringResource(R.string.unsubscribe),
                        color = Color.White,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        Channel(
            item = ChannelItem.OfflineFirstChannelItem(
                url = "",
                name = "channel name",
                description = "description",
                isVerified = true,
                subscriberCount = 45552365L,
                avatar = "",
                id = 1,
            ),
            toChannelScreen = {},
            modifier = Modifier,
            subscribe = {},
            unsubscribe = {},
        )
    }
}
