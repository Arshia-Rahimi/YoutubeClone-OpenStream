package com.github.openstream.ui.global.player.components.playerview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.compose.PainterIconButton
import com.github.openstream.core.common.compose.onCondition
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
    isSheetExpanded: Boolean,
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
            .onCondition(isSheetExpanded) {
                pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { showController = true },
                        onDoubleTap = { offset ->
                            when {
                                offset.x < width / 3f -> PlayerAction.SeekBackward.send()
                                offset.x > 2 * width / 3f -> PlayerAction.SeekForward.send()
                                else -> Unit
                            }
                        },
                    )
                }
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
        if (showController && isSheetExpanded) {
            PlayerController(
                videoData = videoData,
                isFullScreen = isFullScreen,
                isBuffering = isBuffering,
                isPlaying = isPlaying,
                currentPosition = currentPosition,
            )
        }
    }
    
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("SourceLockedOrientationActivity")
private fun BoxScope.PlayerController(
    videoData: VideoData,
    isFullScreen: Boolean,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentPosition: Long,
) {
    var lastSeekPosition by remember { mutableFloatStateOf(currentPosition / videoData.duration.toFloat()) }
    
    Column(
        modifier = Modifier
            .matchParentSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = videoData.name,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            color = Color.White,
            fontSize = 12.sp,
        )
        
        when {
            isBuffering -> CircularProgressIndicator(
                modifier = Modifier.size(40.dp)
            )
            
            isPlaying -> PainterIconButton(
                modifier = Modifier.size(40.dp),
                onClick = PlayerAction.Pause::send,
                drawableRes = R.drawable.pause,
                contentDescription = "pause",
                tint = Color.Unspecified,
            )
            
            else -> PainterIconButton(
                onClick = PlayerAction.Resume::send,
                drawableRes = R.drawable.play,
                contentDescription = "play",
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp),
            )
        }
        
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Slider(
                modifier = Modifier.height(16.dp),
                value = if (isBuffering) lastSeekPosition else currentPosition / videoData.duration.toFloat(),
                onValueChange = {
                    lastSeekPosition = it
                    PlayerAction.SeekTo(it.toLong() * videoData.duration).send()
                },
                track = {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(Color.Gray),
                    ) {
                        val progressWidth =
                            size.width * (currentPosition.toFloat() / videoData.duration.toFloat())
                                .coerceIn(0f, 1f)
                        
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
                            radius = 8f,
                        )
                    }
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = currentPosition.toTime() + " / " + videoData.duration.toTime(),
                    color = Color.White,
                    maxLines = 1,
                )
                
                val context = LocalContext.current
                PainterIconButton(
                    onClick = {
                        if (!isFullScreen) (context as Activity).requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        else (context as Activity).requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    },
                    contentDescription = "fullscreen",
                    tint = Color.Unspecified,
                    drawableRes = if (isFullScreen) R.drawable.fullscreen_enabled else R.drawable.fullscreen_disabled,
                    modifier = Modifier.height(16.dp),
                )
            }
            
        }
    }
}
