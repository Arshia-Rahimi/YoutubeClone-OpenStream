package com.github.openstream.ui.global.reusable.screens.channel.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.github.openstream.ui.designsystem.theme.OpenStreamTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelTopBar(
    item: ChannelItem,
    openBottomSheet: () -> Unit,
    subscribe: (ChannelItem) -> Unit,
    unSubscribe: (ChannelItem) -> Unit,
    channelId: Long?,
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clip(CircleShape)
                        .width(48.dp)
                        .aspectRatio(1f)
                        .clickable { openBottomSheet() },
                    model = item.avatar,
                    contentDescription = "channel avatar",
                )
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
                            fontSize = 16.sp,
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
                when (channelId) {
                    null -> Button(
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

                    else -> Button(
                        onClick = { unSubscribe(item) },
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
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Preview() {
    OpenStreamTheme {
        ChannelTopBar(
            ChannelItem.OnlineChannelItem(
                name = "name",
                subscriberCount = 454443L,
                avatar = "",
                description = "description",
                isVerified = true,
                url = "",
            ),
            openBottomSheet = {},
            subscribe = {},
            channelId = null,
            unSubscribe = {},
        )
    }
}
