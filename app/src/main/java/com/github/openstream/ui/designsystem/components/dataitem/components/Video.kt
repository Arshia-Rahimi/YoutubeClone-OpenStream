package com.github.openstream.ui.designsystem.components.dataitem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.compose.DirectionalText
import com.github.openstream.core.common.util.timeAgo
import com.github.openstream.core.common.util.toShortForm
import com.github.openstream.core.common.util.toTime
import com.github.openstream.core.shared.StreamType
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.ui.designsystem.theme.OpenStreamTheme
import com.github.openstream.ui.global.player.PlayerAction
import com.github.openstream.ui.global.reusable.popups.PopupController

@Composable
fun Video(
    modifier: Modifier,
    item: VideoItem,
    shouldViewChannel: Boolean,
    toChannelScreen: (String) -> Unit,
    saveToWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromWatchLater: ((VideoItem) -> Unit)? = null,
    removeFromPlaylist: ((VideoItem) -> Unit)? = null,
) {
    var isDropDownExpanded by remember { mutableStateOf(false) }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { PlayerAction.Start(item).send() }
            .padding(horizontal = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(4.dp)),
        ) {
            AsyncImage(
                model = item.thumbnail,
                contentDescription = "thumbnail",
                modifier = Modifier.matchParentSize(),
            )
            val text = when (item.streamType) {
                StreamType.LIVE_STREAM -> stringResource(R.string.live)
                else -> item.duration.toTime()
            }
            val color = when (item.streamType) {
                StreamType.LIVE_STREAM -> MaterialTheme.colorScheme.primary.copy(0.5f)
                else -> Color.Black.copy(0.5f)
            }
            Box(
                modifier = Modifier
                    .padding(bottom = 2.dp, end = 2.dp)
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 2.dp),
                    text = text,
                    fontSize = 10.sp,
                    color = Color.White,
                )
            }
            val progress = item.position.toFloat() / item.duration
            Canvas(
                modifier = Modifier.fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.BottomStart),
            ) {
                clipRect {
                    drawRect(
                        color = Color(0xFFCC2849),
                        topLeft = Offset(0f, 0f),
                        size = Size(progress * size.width, size.height),
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 4.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Top,
        ) {
            DirectionalText(
                text = item.name,
                fontSize = 16.sp,
                color = Color.White,
                maxLines = 2,
                lineHeight = 20.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.channelName,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    color = Color(0xFFAAAAAA),
                    maxLines = 1,
                )
                if (item.isChannelVerified) {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        painter = painterResource(R.drawable.verified),
                        contentDescription = "verified",
                        tint = Color(0xFFAAAAAA)
                    )
                }
            }
            Text(
                text = timeString(item),
                fontSize = 10.sp,
                color = Color(0xFFAAAAAA),
                lineHeight = 12.sp,
                maxLines = 1,
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Top)
                .size(24.dp),
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
                        text = { Text(stringResource(R.string.view_channel)) },
                        onClick = {
                            isDropDownExpanded = false
                            toChannelScreen(item.channelUrl)
                        },
                    )
                }
                saveToWatchLater?.let {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.add_to_watch_later)) },
                        onClick = {
                            isDropDownExpanded = false
                            it.invoke(item)
                        },
                    )
                }
                removeFromWatchLater?.let {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.remove_from_watch_later)) },
                        onClick = {
                            isDropDownExpanded = false
                            it.invoke(item)
                        },
                    )
                }
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.add_to_playlist)) },
                    onClick = {
                        isDropDownExpanded = false
                        PopupController.openSaveVideoToPlaylistModal(item)
                    },
                )
                removeFromPlaylist?.let {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.removeFrom_playlist)) },
                        onClick = { it.invoke(item) },
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

private fun timeString(item: VideoItem) =
    item.uploadDate?.let {
        "${item.viewCount.toShortForm()} views • " + when (item.streamType) {
            StreamType.NORMAL -> ""
            StreamType.LIVE_STREAM -> "started streaming"
            StreamType.POST_LIVE_STREAM -> "streamed "
        } + it.timeAgo()
    } ?: ""

@Preview
@Composable
private fun Preview() {
    OpenStreamTheme {
        Video(
            item = VideoItem(
                name = "name",
                channelUrl = "",
                isShort = false,
                channelName = "channel",
                viewCount = 545664L,
                isChannelVerified = true,
                shortDescription = "description",
                streamType = StreamType.LIVE_STREAM,
                channelAvatars = "",
                url = "",
                thumbnail = "",
                uploadDate = null,
                duration = 20000L,
                position = 10000L,
            ),
            toChannelScreen = {},
            shouldViewChannel = true,
            modifier = Modifier,
        )
    }
}
