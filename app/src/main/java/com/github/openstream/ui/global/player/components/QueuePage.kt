package com.github.openstream.ui.global.player.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.compose.PainterIconButton
import com.github.openstream.core.common.compose.onCondition
import com.github.openstream.core.common.util.toTime
import com.github.openstream.core.shared.MiniPlayerConfig
import com.github.openstream.core.shared.StreamType
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.ui.designsystem.theme.OpenStreamTheme
import com.github.openstream.ui.global.player.PlayerAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueuePage(
    isPlaying: Boolean,
    queue: List<VideoItem>,
    currentPosition: Long,
    currentVideo: VideoItem?,
    isAudioOnlyModeEnabled: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            items(queue) { video ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { PlayerAction.PlayFromItem(video).send() }
                        .onCondition(video == currentVideo) { background(Color.Gray) }
                        .padding(horizontal = 4.dp),
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
                .weight(0.1f)
                .padding(horizontal = 16.dp),
        ) {
            val progress = (currentPosition.toFloat()/(currentVideo?.duration?.toFloat() ?: 1f)).coerceIn(0f, 1f)
            Slider(
                track = {
                    Canvas(
                        modifier = Modifier.fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.Gray),
                    ) {
                        val progressWidth = size.width * progress
                        clipRect {
                            drawRect(
                                color = Color(0xFFCC2849),
                                topLeft = Offset(0f, 0f),
                                size = Size(progressWidth, size.height),
                            )
                        }
                    }
                },
                thumb = {
                    Canvas(
                        modifier = Modifier.size(16.dp),
                    ) { 
                        drawCircle(
                            color = Color(0xFFCC2849),
                            radius = 16f,
                        )
                    }
                },
                value = progress,
                onValueChange = { position: Float ->
                    PlayerAction.SeekTo(position.toLong() * (currentVideo?.duration ?: 1L)).send()
                },
                modifier = Modifier
                    .height(MiniPlayerConfig.VIDEO_PROGRESS_INDICATOR_THICKNESS.dp)
                    .fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                ),
            )

            Row(
                modifier = Modifier.fillMaxSize()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                PainterIconButton(
                    onClick = PlayerAction.ToggleAudioOnlyMode::send,
                    drawableRes = if (isAudioOnlyModeEnabled) R.drawable.audio_only_enabled else R.drawable.audio_only_disabled,
                    contentDescription = "audio only mode",
                    tint = Color.Unspecified,
                )
                PainterIconButton(
                    onClick = PlayerAction.Previous::send,
                    drawableRes = R.drawable.previous,
                    tint = Color.White,
                )
                PainterIconButton(
                    onClick = PlayerAction.TogglePlay::send,
                    drawableRes = if (isPlaying) R.drawable.pause else R.drawable.play,
                    tint = Color.White,
                )
                PainterIconButton(
                    onClick = PlayerAction.Next::send,
                    drawableRes = R.drawable.next,
                    tint = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    OpenStreamTheme {
        QueuePage(
            isPlaying = false,
            queue = listOf(
                VideoItem(
                    name = "video",
                    thumbnail = "",
                    url = "",
                    streamType = StreamType.NORMAL,
                    channelName = "channel",
                    shortDescription = "description",
                    uploadDate = null,
                    viewCount = 40000L,
                    duration = 50000L,
                    channelUrl = "",
                    isChannelVerified = true,
                    isShort = false,
                    channelAvatars = "",
                    id = null,
                )
            ),
            currentPosition = 10000,
            currentVideo = VideoItem(
                name = "video",
                thumbnail = "",
                url = "",
                streamType = StreamType.NORMAL,
                channelName = "channel",
                shortDescription = "description",
                uploadDate = null,
                viewCount = 40000L,
                duration = 50000,
                channelUrl = "",
                isChannelVerified = true,
                isShort = false,
                channelAvatars = "",
                id = null,
            ),
            isAudioOnlyModeEnabled = true,
        )
    }
}
