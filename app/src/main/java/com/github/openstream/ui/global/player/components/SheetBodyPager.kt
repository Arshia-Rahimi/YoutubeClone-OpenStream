package com.github.openstream.ui.global.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.compose.PainterIconButton
import com.github.openstream.core.common.compose.onCondition
import com.github.openstream.core.common.util.toTime
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.model.dataitem.VideoItem
import com.github.openstream.core.model.extractordata.VideoData
import com.github.openstream.core.model.extractordata.VideoOption
import com.github.openstream.core.shared.VIDEO_PROGRESS_INDICATOR_THICKNESS
import com.github.openstream.ui.global.player.PlayerAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SheetBodyPager(
    queue: List<VideoItem>,
    isPlaying: Boolean,
    sheetDragProgress: Float,
    fetchingState: OpenStreamMediaPlayer.FetchingState,
    currentVideoData: VideoData?,
    currentVideo: VideoItem?,
    currentPosition: Long,
    scope: CoroutineScope,
    videoPlaylistsState: VideoPlaylistsState,
    toChannelScreen: (String) -> Unit,
    toggleVideoWatchLater: () -> Unit,
    toggleVideoLiked: () -> Unit,
    switchPlaybackQuality: (VideoOption) -> Unit,
) {
    val pagerState = rememberPagerState { SheetBodyPage.entries.size }
    if (sheetDragProgress != 0f) Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(sheetDragProgress)
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) { currentTab ->
            when (currentTab) {
                SheetBodyPage.VideoDescription.ordinal ->
                    VideoDescriptionPage(
                        fetchingState = fetchingState,
                        currentVideoData = currentVideoData,
                        toChannelScreen = toChannelScreen,
                        scope = scope,
                        toggleVideoWatchLater = toggleVideoWatchLater,
                        toggleVideoLiked = toggleVideoLiked,
                        videoPlaylistsState = videoPlaylistsState,
                        switchPlaybackQuality = switchPlaybackQuality,
                    )

                SheetBodyPage.Queue.ordinal ->
                    QueuePage(
                        isPlaying = isPlaying,
                        queue = queue,
                        currentVideo = currentVideo,
                        currentPosition = currentPosition,
                    )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SheetBodyPage.entries.forEach { tab ->
                IconButton(
                    onClick = { scope.launch { pagerState.scrollToPage(page = tab.ordinal) } },
                    modifier = Modifier.weight(0.5f),
                ) {
                    Icon(
                        painter = painterResource(if (tab.ordinal == pagerState.currentPage) tab.selectedIcon else tab.icon),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoDescriptionPage(
    fetchingState: OpenStreamMediaPlayer.FetchingState,
    currentVideoData: VideoData?,
    scope: CoroutineScope,
    toChannelScreen: (String) -> Unit,
    toggleVideoWatchLater: () -> Unit,
    toggleVideoLiked: () -> Unit,
    videoPlaylistsState: VideoPlaylistsState,
    switchPlaybackQuality: (VideoOption) -> Unit,
) {
    when (fetchingState) {
        is OpenStreamMediaPlayer.FetchingState.Success -> currentVideoData?.let { currentVideo ->
            SheetBody(
                modifier = Modifier.fillMaxSize(),
                videoData = currentVideo,
                scrollState = rememberScrollState(),
                scope = scope,
                toChannelScreen = toChannelScreen,
                shareVideo = {},
                likeVideo = { toggleVideoLiked() },
                addToWatchLater = { toggleVideoWatchLater() },
                videoPlaylistsState = videoPlaylistsState,
                switchPlaybackQuality = switchPlaybackQuality,
            )
        }

        is OpenStreamMediaPlayer.FetchingState.Loading ->
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

        is OpenStreamMediaPlayer.FetchingState.Error ->
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(fetchingState.message ?: "")
            }
    }
}


@Composable
private fun QueuePage(
    isPlaying: Boolean,
    queue: List<VideoItem>,
    currentPosition: Long,
    currentVideo: VideoItem?,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 12.dp),
        ) {
            items(queue) { video ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { PlayerAction.PlayFromItem(video).send() }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .onCondition(video == currentVideo) { background(Color.Gray) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = video.thumbnail,
                        contentDescription = "thumbnail",
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(16 / 9f)
                    )
                    Text(
                        text = video.name,
                        fontSize = 12.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .basicMarquee(),
                    )
                    Text(
                        text = video.duration.toTime(),
                        fontSize = 12.sp,
                        color = Color.White,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Slider(
                value = currentPosition.toFloat(),
                onValueChange = { position: Float ->
                    println(position)
                    PlayerAction.SeekTo(position.toLong() * 1000).send()
                },
                valueRange = 0f..(currentVideo?.duration?.toFloat() ?: 1f),
                modifier = Modifier
                    .height(VIDEO_PROGRESS_INDICATOR_THICKNESS.dp)
                    .fillMaxWidth(),
                colors = SliderDefaults.colors(
                    // todo
                ),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                PainterIconButton(
                    onClick = { PlayerAction.Previous.send() },
                    drawableRes = R.drawable.skip_previous,
                    tint = Color.White,
                )
                PainterIconButton(
                    onClick = { PlayerAction.TogglePlay.send() },
                    drawableRes = if (isPlaying) R.drawable.pause else R.drawable.play,
                    tint = Color.White,
                )
                PainterIconButton(
                    onClick = { PlayerAction.Next.send() },
                    drawableRes = R.drawable.skip_next,
                    tint = Color.White,
                )
            }
        }
    }
}
