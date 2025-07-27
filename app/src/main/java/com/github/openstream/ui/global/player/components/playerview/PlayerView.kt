package com.github.openstream.ui.global.player.components.playerview

import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.compose.PainterIconButton
import com.github.openstream.core.common.util.toTime
import com.github.openstream.core.shared.extractor.data.VideoData
import com.github.openstream.ui.global.player.PlayerAction
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun PlayerView(
    videoData: VideoData,
    modifier: Modifier = Modifier,
    player: Player,
    isAudioModeEnabled: Boolean,
    isFullScreen: Boolean,
    isBuffering: Boolean,
    currentPosition: Long,
    isPlaying: Boolean,
) {
    var showController by remember { mutableStateOf(false) }
    var width by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(showController) {
        if (showController) {
            delay(5000L)
            showController = false
        }
    }
    
    Box(
        modifier = modifier
            .onGloballyPositioned {
                width = it.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { showController = true },
                    onDoubleTap = { offset ->
                        if (showController) return@detectTapGestures
                        when {
                            offset.x < width / 3f -> PlayerAction.SeekBackward::send
                            offset.x < 2 * width / 3f -> PlayerAction.SeekForward::send
                            else -> Unit
                        }
                    },
                )
            },
    ) {
        if (isAudioModeEnabled) {
            AsyncImage(
                model = videoData.thumbnail,
                contentDescription = "thumbnail",
                modifier = Modifier.matchParentSize(),
            )
        } else {
            AndroidView(
                modifier = Modifier.matchParentSize(),
                factory = { context ->
                    PlayerView(context).also {
                        it.player = player
                        it.useController = false
                        it.setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
                    }
                },
            )
        }
        PlayerController(
            videoData = videoData,
            isFullScreen = isFullScreen,
            isBuffering = isBuffering,
            isPlaying = isPlaying,
            currentPosition = currentPosition,
        )
    }
    
}

@Composable
private fun BoxScope.PlayerController(
    videoData: VideoData,
    isFullScreen: Boolean,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentPosition: Long,
) {
    Column(
        modifier = Modifier.matchParentSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = videoData.name,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        
        when {
            isBuffering -> CircularProgressIndicator(Modifier.size(4.dp))
            isPlaying -> PainterIconButton(
                onClick = PlayerAction.Resume::send,
                drawableRes = R.drawable.play,
                contentDescription = "play",
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp),
            )
            
            else -> PainterIconButton(
                modifier = Modifier.size(40.dp),
                onClick = PlayerAction.Pause::send,
                drawableRes = R.drawable.pause,
                contentDescription = "pause",
                tint = Color.Unspecified,
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = currentPosition.toTime() + " / " + videoData.duration.toTime(),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
            )
            PainterIconButton(
                onClick = {},
                contentDescription = "fullscreen",
                tint = Color.Unspecified,
                drawableRes = if (isFullScreen) R.drawable.fullscreen_enabled else R.drawable.fullscreen_disabled,
            )
        }
        
    }
}
