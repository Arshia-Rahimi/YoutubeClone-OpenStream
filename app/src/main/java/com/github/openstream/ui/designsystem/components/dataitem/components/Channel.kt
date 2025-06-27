package com.github.openstream.ui.designsystem.components.dataitem.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.util.toShortForm
import com.github.openstream.core.model.dataitem.ChannelItem
import com.github.openstream.ui.global.popups.PopupController
import com.github.openstream.ui.global.popups.confirmationdialog.UnsubscribeItem

@Composable
fun Channel(
    modifier: Modifier,
    item: ChannelItem,
    toChannelScreen: (String) -> Unit,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { toChannelScreen(item.url) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.4f)
                .padding(end = 4.dp),
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
                fontSize = 12.sp,
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
                    )
                }
            }

            is ChannelItem.OfflineFirstChannelItem -> {
                Button(
                    onClick = { PopupController.openConfirmationDialog(UnsubscribeItem(item)) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF242424),
                    )
                ) {
                    Text(
                        text = stringResource(R.string.unsubscribe),
                        color = Color.White,
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
        )
    }
}
