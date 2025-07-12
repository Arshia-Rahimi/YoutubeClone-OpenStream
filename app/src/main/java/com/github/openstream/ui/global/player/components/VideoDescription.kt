package com.github.openstream.ui.global.player.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.openstream.R
import com.github.openstream.core.common.util.timeAgo
import com.github.openstream.core.common.util.toShortForm
import com.github.openstream.core.model.dataitem.ChannelItem
import com.github.openstream.core.model.dataitem.StreamType
import com.github.openstream.core.model.dataitem.VideoItem
import com.github.openstream.core.model.extractordata.VideoData
import com.github.openstream.core.model.extractordata.VideoOption
import com.github.openstream.core.model.extractordata.VideoQuality
import com.github.openstream.ui.designsystem.components.dataitem.components.Channel
import com.github.openstream.ui.global.popups.PopupController

@Composable
fun VideoDescription(
    videoData: VideoData,
    scrollState: ScrollState,
    videoPlaylistsState: VideoPlaylistsState,
    currentQuality: VideoQuality?,
    toChannelScreen: (String) -> Unit,
    shareVideo: (VideoItem) -> Unit,
    likeVideo: () -> Unit,
    addToWatchLater: () -> Unit,
    switchPlaybackQuality: (VideoOption) -> Unit,
) {
    val videoItem = remember { videoData.toDataItem() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = videoData.name,
                fontSize = 20.sp,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            )
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
        Channel(
            modifier = Modifier,
            toChannelScreen = toChannelScreen,
            subscribe = {
                // todo
            },
            item = ChannelItem.OnlineChannelItem(
                url = videoData.channelUrl,
                name = videoData.channelName,
                isVerified = videoData.isChannelVerified,
                subscriberCount = videoData.subscriberCount,
                description = "",
                avatar = videoData.channelAvatar,
            )
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                currentQuality?.let {
                    var showQualityOptions by remember { mutableStateOf(false) }
                    OptionsRowItem(
                        modifier = Modifier.clickable {
                            showQualityOptions = true
                        }
                    ) {
                        Text(
                            text = "${it.quality}p",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 4.dp),
                        )
                    }
                    DropdownMenu(
                        expanded = showQualityOptions,
                        onDismissRequest = { showQualityOptions = false },
                    ) {
                        videoData.videoOptions.forEach { q ->
                            DropdownMenuItem(
                                text = {
                                    Text(q.quality.quality.toString())
                                },
                                onClick = {
                                    switchPlaybackQuality(q)
                                    showQualityOptions = false
                                }
                            )
                        }
                    }
                }
            }
            item {
                OptionsRowItem {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        IconButton(
                            onClick = likeVideo,
                            modifier = Modifier.fillMaxHeight(),
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
                            modifier = Modifier.padding(end = 12.dp),
                        )
                    }
                }
            }
            item {
                OptionsRowItem {
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
            }
            item {
                OptionsRowItem {
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
            }
            item {
                OptionsRowItem {
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
        Text(
            text = videoData.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .verticalScroll(scrollState),
        )
    }
}

@Composable
private fun OptionsRowItem(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .widthIn(min = 40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF292929)),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        Column(Modifier.fillMaxSize()) {
            VideoDescription(
                toChannelScreen = {},
                videoData = VideoData(
                    name = "name",
                    description = "description",
                    channelAvatar = "",
                    isChannelVerified = true,
                    duration = 34324L,
                    viewCount = 454222L,
                    channelUrl = "",
                    channelName = "channel",
                    url = "",
                    videoOptions = emptyList(),
                    audioStream = "",
                    streamType = StreamType.NORMAL,
                    subscriberCount = 454321L,
                    likeCount = 3234L,
                    uploadDate = null,
                ),
                scrollState = rememberScrollState(),
                likeVideo = {},
                shareVideo = {},
                addToWatchLater = {},
                videoPlaylistsState = VideoPlaylistsState(),
                switchPlaybackQuality = {},
                currentQuality = VideoQuality.Q144p,
            )
        }
    }
}
