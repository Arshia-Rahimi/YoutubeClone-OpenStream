package com.github.openstream.ui.global.screens.channel.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.openstream.core.common.util.toShortForm
import com.github.openstream.core.shared.dataitem.ChannelItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelTopBar(
    info: ChannelItem,
    openBottomSheet: () -> Unit,
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
                    model = info.avatar,
                    contentDescription = "channel avatar",
                )
                Text(
                    text = info.name,
                    modifier = Modifier
                        .clickable { openBottomSheet() },
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = info.subscriberCount.toShortForm() + " subscribers",
                    modifier = Modifier,
                    fontSize = 16.sp,
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Preview() {
    MaterialTheme {
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
        )
    }
}
