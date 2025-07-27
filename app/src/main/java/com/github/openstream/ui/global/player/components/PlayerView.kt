package com.github.openstream.ui.global.player.components

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.compose.onCondition
import com.github.openstream.core.common.util.toTime
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.shared.extractor.data.VideoData
import com.github.openstream.ui.designsystem.components.noRippleClickable
import com.github.openstream.ui.global.player.PlayerAction
import com.github.openstream.ui.global.player.PlayerViewModel
import com.github.openstream.ui.global.player.model.PlayerSheetState
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<PlayerViewModel>()
    
    val fetchingState by viewModel.fetchingState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val bufferedPosition by viewModel.bufferedPosition.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val isBuffering by viewModel.isBuffering.collectAsStateWithLifecycle()
    val isAudioOnlyModeEnabled by viewModel.isAudioOnlyModeEnabled.collectAsStateWithLifecycle()
    val isInLandscape by viewModel.isInLandscape.collectAsStateWithLifecycle()
    
    val sheetState by viewModel.sheetState.collectAsStateWithLifecycle()
    val isSheetExpanded by remember { derivedStateOf { sheetState == PlayerSheetState.EXPANDED } }
    
    var showController by remember { mutableStateOf(false) }
    var width by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(showController) {
        if (showController) {
            delay(5000L)
            showController = false
        }
    }
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(16 / 9f)
            .onCondition(
                condition = fetchingState !is OpenStreamMediaPlayer.FetchingState.Success,
                onFalse = { background(Color.Black) },
                onTrue = { background(MaterialTheme.colorScheme.tertiaryContainer) },
            )
            .onGloballyPositioned {
                width = it.size.width.toFloat()
            }
            .onCondition(isSheetExpanded) {
                pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { if (isSheetExpanded) showController = !showController },
                        onDoubleTap = { offset ->
                            if (!isSheetExpanded) return@detectTapGestures
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
        when (fetchingState) {
            is OpenStreamMediaPlayer.FetchingState.Loading -> CircularProgressIndicator()
            is OpenStreamMediaPlayer.FetchingState.Error -> {
                Icon(
                    painter = painterResource(R.drawable.cross),
                    contentDescription = "",
                    tint = Color.White,
                )
            }
            
            is OpenStreamMediaPlayer.FetchingState.Success -> {
                PlayerView(
                    isInLandscape = isInLandscape,
                    player = viewModel.playerInstance,
                    isBuffering = isBuffering,
                    videoData = (fetchingState as OpenStreamMediaPlayer.FetchingState.Success).video,
                    isPlaying = isPlaying,
                    currentPosition = currentPosition,
                    showController = showController,
                    isSheetExpanded = isSheetExpanded,
                    bufferedPosition = bufferedPosition,
                    isAudioOnlyModeEnabled = isAudioOnlyModeEnabled,
                )
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun BoxScope.PlayerView(
    showController: Boolean,
    videoData: VideoData,
    isAudioOnlyModeEnabled: Boolean,
    player: Player,
    isInLandscape: Boolean,
    isBuffering: Boolean,
    currentPosition: Long,
    bufferedPosition: Long,
    isPlaying: Boolean,
    isSheetExpanded: Boolean,
) {
    if (isAudioOnlyModeEnabled) {
        AsyncImage(
            model = videoData.thumbnail,
            contentDescription = "thumbnail",
            modifier = Modifier.matchParentSize(),
        )
    } else {
        AndroidView(
            modifier = Modifier
                .matchParentSize(),
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
            isInLandscape = isInLandscape,
            isBuffering = isBuffering,
            isPlaying = isPlaying,
            currentPosition = currentPosition,
            bufferedPosition = bufferedPosition,
        )
    }
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("SourceLockedOrientationActivity")
private fun BoxScope.PlayerController(
    videoData: VideoData,
    isInLandscape: Boolean,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentPosition: Long,
    bufferedPosition: Long,
) {
    // todo: fix player controller in full screen
    
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
                .padding(top = 2.dp)
                .align(Alignment.Start),
            color = Color.White,
            fontSize = 16.sp,
        )
        
        when {
            !isPlaying -> IconButton(
                onClick = PlayerAction.Resume::send,
            ) {
                Icon(
                    painter = painterResource(R.drawable.play),
                    contentDescription = "play",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(60.dp),
                )
            }
            
            isBuffering -> CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .noRippleClickable(PlayerAction.Pause::send)
            )
            
            else -> IconButton(
                onClick = PlayerAction.Pause::send,
            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(R.drawable.pause),
                    contentDescription = "pause",
                    tint = Color.Unspecified,
                )
            }
        }
        
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val progress = currentPosition / videoData.duration.toFloat()
            var sliderProgress by remember { mutableFloatStateOf(0f) }
            var isUserChangingSliderValue by remember { mutableStateOf(false) }
            
            // todo: fix the thumb
            LaunchedEffect(isBuffering, isUserChangingSliderValue, progress) {
                if (!isUserChangingSliderValue && !isBuffering) {
                    sliderProgress = progress
                }
            }
            
            Slider(
                modifier = Modifier.height(8.dp),
                value = sliderProgress,
                onValueChange = {
                    isUserChangingSliderValue = true
                    sliderProgress = it
                },
                onValueChangeFinished = {
                    PlayerAction.SeekTo((sliderProgress * videoData.duration).toLong()).send()
                    isUserChangingSliderValue = false
                },
                track = {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(Color(0xFF808080)),
                    ) {
                        val bufferedWidth =
                            size.width * (bufferedPosition.toFloat() / videoData.duration.toFloat())
                                .coerceIn(0f, 1f)
                        val progressWidth = size.width * it.value
                        
                        clipRect {
                            drawRect(
                                color = Color(0xFFCACACA),
                                topLeft = Offset(0f, 0f),
                                size = Size(bufferedWidth, size.height),
                            )
                        }
                        
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
                            radius = 12f,
                        )
                    }
                },
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = (currentPosition / 1000).toTime() + " / " + (videoData.duration / 1000).toTime(),
                    color = Color.White,
                    maxLines = 1,
                )
                
                val context = LocalContext.current
                IconButton(
                    onClick = {
                        if (!isInLandscape) (context as Activity).requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        else (context as Activity).requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                ) {
                    Icon(
                        contentDescription = "fullscreen",
                        tint = Color.Unspecified,
                        painter = painterResource(if (isInLandscape) R.drawable.fullscreen_enabled else R.drawable.fullscreen_disabled),
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
            
        }
    }
}
