package com.github.arshiarahimi.openstream.ui.global.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.arshiarahimi.openstream.R
import com.github.arshiarahimi.openstream.core.common.util.timeAgo
import com.github.arshiarahimi.openstream.core.common.util.toShortForm
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.StreamType
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.model.extractordata.VideoData
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.components.Channel
import com.github.arshiarahimi.openstream.ui.global.popups.PopupController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SheetBody(
    modifier: Modifier,
    videoData: VideoData,
    scrollState: ScrollState,
    scope: CoroutineScope,
    videoPlaylistsState: VideoPlaylistsState,
    toChannelScreen: (String) -> Unit,
    shareVideo: (VideoItem) -> Unit,
    likeVideo: () -> Unit,
    addToWatchLater: () -> Unit,
) {
    val rowScroll = rememberScrollState()
    var showDescription by remember { mutableStateOf(false) }
    val videoItem = remember { videoData.toDataItem() }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable { scope.launch { showDescription = !showDescription } },
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = videoData.name,
                    fontSize = 20.sp,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    painter = painterResource(if (showDescription) R.drawable.condense else R.drawable.expand),
                    contentDescription = "description sheet",
                    tint = MaterialTheme.colorScheme.onTertiary,
                )
            }
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.view),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary,
                )
                Text(
                    text = videoData.viewCount.toShortForm(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
                Icon(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary,
                )
                Text(
                    text = videoData.uploadDate?.timeAgo() ?: "",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
            }
        }
        AnimatedVisibility(
            visible = showDescription,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1f),
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Text(
                text = videoData.description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.verticalScroll(scrollState),
            )
        }
        Channel(
            modifier = Modifier,
            toChannelScreen = toChannelScreen,
            subscribe = {},
            item = ChannelItem.OnlineChannelItem(
                url = videoData.channelUrl,
                name = videoData.channelName,
                isVerified = videoData.isChannelVerified,
                subscriberCount = videoData.subscriberCount,
                description = "",
                avatar = videoData.channelAvatar,
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .horizontalScroll(rowScroll),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF292929))
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = likeVideo,
                ) {
                    Icon(
                        painter = painterResource(if (videoPlaylistsState.isLiked) R.drawable.like_filled else R.drawable.like),
                        contentDescription = "like video",
                        tint = Color.White,
                    )
                }
                Text(
                    text = videoData.likeCount.toShortForm(),
                    color = Color.White,
                )
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF292929)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { shareVideo(videoItem) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.share),
                        contentDescription = "share video",
                        tint = Color.White,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF292929)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = addToWatchLater,
                ) {
                    Icon(
                        painter = painterResource(if (videoPlaylistsState.isInWatchLater) R.drawable.watch_later_filled else R.drawable.watch_later),
                        contentDescription = "add to watch later",
                        tint = Color.White,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF292929)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { PopupController.openSaveVideoToPlaylistModal(videoItem) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.addtoplaylist),
                        contentDescription = "add to playlist",
                        tint = Color.White,
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
        Column(Modifier.fillMaxSize()) {
            SheetBody(
                toChannelScreen = {},
                videoData = VideoData(
                    name = "name",
                    description = "description",
                    channelAvatar = "",
                    isChannelVerified = true,
                    length = 34324L,
                    viewCount = 454222L,
                    channelUrl = "",
                    channelName = "channel",
                    url = "",
                    videoStreams = emptyList(),
                    audioStreams = emptyList(),
                    videoOnlyStreams = emptyList(),
                    streamType = StreamType.NORMAL,
                    subtitles = emptyList(),
                    subscriberCount = 454321L,
                    likeCount = 3234L,
                    uploadDate = null,
                ),
                scrollState = rememberScrollState(),
                likeVideo = {},
                shareVideo = {},
                addToWatchLater = {},
                modifier = Modifier,
                scope = rememberCoroutineScope(),
                videoPlaylistsState = VideoPlaylistsState(),
            )
        }
    }
}
