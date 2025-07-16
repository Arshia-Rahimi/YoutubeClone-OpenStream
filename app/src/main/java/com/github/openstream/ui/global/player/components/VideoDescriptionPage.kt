package com.github.openstream.ui.global.player.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.shared.StreamType
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.VideoData
import com.github.openstream.core.shared.extractor.data.VideoOption
import com.github.openstream.core.shared.extractor.data.VideoQuality
import com.github.openstream.ui.designsystem.components.dataitem.components.Channel
import com.github.openstream.ui.designsystem.theme.OpenStreamTheme
import com.github.openstream.ui.global.player.model.VideoLocalState
import com.github.openstream.ui.global.reusable.popups.PopupController

@Composable
fun VideoDescriptionPage(
    fetchingState: OpenStreamMediaPlayer.FetchingState,
    currentVideoData: VideoData?,
    currentQuality: VideoQuality?,
    toChannelScreen: (String) -> Unit,
    toggleVideoWatchLater: () -> Unit,
    toggleVideoLiked: () -> Unit,
    videoLocalState: VideoLocalState,
    switchPlaybackQuality: (VideoOption) -> Unit,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit,
    collapseMiniPlayer: () -> Unit,
) {
    when (fetchingState) {
        is OpenStreamMediaPlayer.FetchingState.Success -> currentVideoData?.let { currentVideo ->
            VideoDescription(
                videoData = currentVideo,
                scrollState = rememberScrollState(),
                currentQuality = currentQuality,
                toChannelScreen = toChannelScreen,
                shareVideo = {},
                likeVideo = toggleVideoLiked,
                addToWatchLater = toggleVideoWatchLater,
                videoLocalState = videoLocalState,
                switchPlaybackQuality = switchPlaybackQuality,
                subscribe = subscribe,
                collapseMiniPlayer = collapseMiniPlayer,
            )
        }

        is OpenStreamMediaPlayer.FetchingState.Loading ->
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

        is OpenStreamMediaPlayer.FetchingState.Error ->
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(fetchingState.message ?: "", color = Color.White)
            }
    }
}

@Composable
fun VideoDescription(
    videoData: VideoData,
    scrollState: ScrollState,
    videoLocalState: VideoLocalState,
    currentQuality: VideoQuality?,
    toChannelScreen: (String) -> Unit,
    shareVideo: (VideoItem) -> Unit,
    likeVideo: () -> Unit,
    addToWatchLater: () -> Unit,
    switchPlaybackQuality: (VideoOption) -> Unit,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit,
    collapseMiniPlayer: () -> Unit,
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
                    .padding(horizontal = 8.dp)
                    .basicMarquee(),
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
            toChannelScreen = {
                toChannelScreen(it)
                collapseMiniPlayer()
            },
            subscribe = subscribe,
            item = if (videoLocalState.isChannelSubscribed) ChannelItem.OfflineFirstChannelItem(
                url = videoData.channelUrl,
                name = videoData.channelName,
                isVerified = videoData.isChannelVerified,
                subscriberCount = videoData.subscriberCount,
                description = "",
                avatar = videoData.channelAvatar,
                id = Long.MIN_VALUE, // id is not used in the composable, this is a placeholder
            ) else ChannelItem.OnlineChannelItem(
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
                                painter = painterResource(if (videoLocalState.isLiked) R.drawable.like_filled else R.drawable.like),
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
                            painter = painterResource(if (videoLocalState.isInWatchLater) R.drawable.watch_later_filled else R.drawable.watch_later),
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
    OpenStreamTheme {
        VideoDescriptionPage(
            fetchingState = OpenStreamMediaPlayer.FetchingState.Success,
            currentVideoData =
                VideoData(
                    likeCount = 1000,
                    name = "video",
                    channelAvatar = "",
                    url = "",
                    streamType = StreamType.NORMAL,
                    channelName = "channel",
                    description = "description",
                    uploadDate = null,
                    viewCount = 40000L,
                    duration = 50000,
                    channelUrl = "",
                    isChannelVerified = true,
                    audioStream = "",
                    videoOptions = emptyList(),
                    subscriberCount = 435627L,
                ),
            currentQuality = VideoQuality.Q144p,
            toChannelScreen = {},
            toggleVideoLiked = {},
            videoLocalState = VideoLocalState(),
            switchPlaybackQuality = {},
            toggleVideoWatchLater = {},
            subscribe = {},
            collapseMiniPlayer = {},
        )
    }
}
