package com.github.openstream.ui.global.player.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.compose.PainterIconButton
import com.github.openstream.core.common.compose.onCondition
import com.github.openstream.core.common.util.toTime
import com.github.openstream.core.model.dataitem.VideoItem
import com.github.openstream.core.shared.MiniPlayerConfig
import com.github.openstream.ui.global.player.PlayerAction

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
                    PlayerAction.SeekTo(position.toLong() * 1000).send()
                },
                valueRange = 0f..(currentVideo?.duration?.toFloat() ?: 1f),
                modifier = Modifier
                    .height(MiniPlayerConfig.VIDEO_PROGRESS_INDICATOR_THICKNESS.dp)
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
                    onClick = PlayerAction.Previous::send,
                    drawableRes = R.drawable.skip_previous,
                    tint = Color.White,
                )
                PainterIconButton(
                    onClick = PlayerAction.TogglePlay::send,
                    drawableRes = if (isPlaying) R.drawable.pause else R.drawable.play,
                    tint = Color.White,
                )
                PainterIconButton(
                    onClick = PlayerAction.Next::send,
                    drawableRes = R.drawable.skip_next,
                    tint = Color.White,
                )
                PainterIconButton(
                    onClick = PlayerAction.ToggleAudioOnlyMode::send,
                    drawableRes = if (isAudioOnlyModeEnabled) R.drawable.audio_only_enabled else R.drawable.audio_only_disabled,
                    contentDescription = "audio only mode",
                    tint = Color.Unspecified,
                )
            }
        }
    }
}
