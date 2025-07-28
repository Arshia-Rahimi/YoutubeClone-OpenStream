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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.openstream.R
import com.github.openstream.core.common.compose.HtmlText
import com.github.openstream.core.common.compose.PainterIconButton
import com.github.openstream.core.common.util.timeAgo
import com.github.openstream.core.common.util.toShortForm
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.VideoData
import com.github.openstream.core.shared.extractor.data.VideoOption
import com.github.openstream.core.shared.extractor.data.VideoQuality
import com.github.openstream.ui.designsystem.components.dataitem.components.Channel
import com.github.openstream.ui.designsystem.theme.OpenStreamTheme
import com.github.openstream.ui.global.player.PlayerAction
import com.github.openstream.ui.global.player.model.PlaybackSpeed
import com.github.openstream.ui.global.player.model.VideoLocalState
import com.github.openstream.ui.global.reusable.popups.PopupController

@Composable
fun VideoDescriptionPage(
    fetchingState: OpenStreamMediaPlayer.FetchingState,
    currentQuality: VideoQuality?,
    isAudioOnlyModeEnabled: Boolean,
    toChannelScreen: (String) -> Unit,
    toggleVideoWatchLater: () -> Unit,
    toggleVideoLiked: () -> Unit,
    videoLocalState: VideoLocalState,
    switchPlaybackQuality: (VideoOption) -> Unit,
    tempVideoName: String,
    playbackSpeed: Float,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit,
    unsubscribe: (ChannelItem.OfflineFirstChannelItem) -> Unit,
    toPlaylistScreen: (String) -> Unit,
    collapseMiniPlayer: () -> Unit,
) {
    when (fetchingState) {
        is OpenStreamMediaPlayer.FetchingState.Success ->
            VideoDescription(
                videoData = fetchingState.video,
                toPlaylistScreen = toPlaylistScreen,
                scrollState = rememberScrollState(),
                currentQuality = currentQuality,
                toChannelScreen = toChannelScreen,
                shareVideo = {},
                toggleVideoLiked = toggleVideoLiked,
                toggleVideoWatchLater = toggleVideoWatchLater,
                videoLocalState = videoLocalState,
                switchPlaybackQuality = switchPlaybackQuality,
                subscribe = subscribe,
                collapseMiniPlayer = collapseMiniPlayer,
                isAudioOnlyModeEnabled = isAudioOnlyModeEnabled,
                playbackSpeed = playbackSpeed,
                unsubscribe = unsubscribe,
            )
        
        
        is OpenStreamMediaPlayer.FetchingState.Error ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(fetchingState.message ?: "", color = Color.White)
                Button(
                    onClick = PlayerAction.Retry::send,
                ) {
                    Text(stringResource(R.string.retry))
                }
            }
        
        is OpenStreamMediaPlayer.FetchingState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = tempVideoName,
                    fontSize = 20.sp,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                )
            }
        }
    }
}

@Composable
fun VideoDescription(
    videoData: VideoData,
    playbackSpeed: Float,
    scrollState: ScrollState,
    videoLocalState: VideoLocalState,
    isAudioOnlyModeEnabled: Boolean,
    currentQuality: VideoQuality?,
    toChannelScreen: (String) -> Unit,
    shareVideo: (VideoItem) -> Unit,
    toggleVideoLiked: () -> Unit,
    toggleVideoWatchLater: () -> Unit,
    switchPlaybackQuality: (VideoOption) -> Unit,
    toPlaylistScreen: (String) -> Unit,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit,
    unsubscribe: (ChannelItem.OfflineFirstChannelItem) -> Unit,
    collapseMiniPlayer: () -> Unit,
) {
    val videoItem = remember { videoData.toDataItem() }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
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
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
                Icon(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary,
                )
                Text(
                    text = videoData.uploadDate?.timeAgo() ?: "",
                    fontSize = 12.sp,
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
            unsubscribe = unsubscribe,
            item = if (videoLocalState.channelId != null) ChannelItem.OfflineFirstChannelItem(
                url = videoData.channelUrl,
                name = videoData.channelName,
                isVerified = videoData.isChannelVerified,
                subscriberCount = videoData.subscriberCount,
                description = "",
                avatar = videoData.channelAvatar,
                id = videoLocalState.channelId,
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                OptionsRowItem {
                    PainterIconButton(
                        onClick = PlayerAction.ToggleAudioOnlyMode::send,
                        drawableRes = if (isAudioOnlyModeEnabled) R.drawable.audio_only_enabled else R.drawable.audio_only_disabled,
                        tint = Color.Unspecified,
                        contentDescription = "audio only mode",
                    )
                }
            }
            item {
                if (!isAudioOnlyModeEnabled && currentQuality != null) {
                    var showQualityOptions by remember { mutableStateOf(false) }
                    OptionsRowItem(
                        modifier = Modifier.clickable {
                            showQualityOptions = true
                        }
                    ) {
                        Text(
                            text = "${currentQuality.quality}p",
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
                    var showPlaybackSpeed by remember { mutableStateOf(false) }
                    OptionsRowItem(
                        modifier = Modifier.clickable {
                            showPlaybackSpeed = true
                        }
                    ) {
                        Text(
                            text = "x$playbackSpeed",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 4.dp),
                        )
                    }
                    DropdownMenu(
                        expanded = showPlaybackSpeed,
                        onDismissRequest = { showPlaybackSpeed = false },
                    ) {
                        PlaybackSpeed.entries.forEach { s ->
                            DropdownMenuItem(
                                text = {
                                    Text(s.string)
                                },
                                onClick = {
                                    PlayerAction.SetPlaybackSpeed(s).send()
                                    showPlaybackSpeed = false
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
                            onClick = toggleVideoLiked,
                            modifier = Modifier.fillMaxHeight(),
                        ) {
                            Icon(
                                painter = painterResource(if (videoLocalState.isLiked) R.drawable.like_filled else R.drawable.like),
                                contentDescription = "like video",
                                tint = Color.White,
                            )
                        }
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
                        onClick = toggleVideoWatchLater,
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
        val context = LocalContext.current
        HtmlText(
            html = videoData.description,
            onLinkClicked = {
                onLinkClicked(
                    currentVideoUrl = videoData.url,
                    context = context,
                    link = it,
                    toPlaylistScreen = { playlistUrl ->
                        toPlaylistScreen(playlistUrl)
                        collapseMiniPlayer()
                    },
                )
            },
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .verticalScroll(scrollState),
            fontSize = 16.sp,
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
            fetchingState = OpenStreamMediaPlayer.FetchingState.Loading,
            currentQuality = VideoQuality.Q144p,
            toChannelScreen = {},
            toggleVideoLiked = {},
            videoLocalState = VideoLocalState(),
            switchPlaybackQuality = {},
            toggleVideoWatchLater = {},
            subscribe = {},
            collapseMiniPlayer = {},
            toPlaylistScreen = {},
            playbackSpeed = 1f,
            isAudioOnlyModeEnabled = false,
            tempVideoName = "asdgahg",
            unsubscribe = {},
        )
    }
}
