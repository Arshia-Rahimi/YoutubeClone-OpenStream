package com.github.freetube.ui.global.player.components

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.arshia.freetube.R
import com.github.freetube.core.media3.PlayerState
import com.github.freetube.core.media3.PlayingStatus
import com.github.freetube.ui.designsystem.components.noRippleClickable

@OptIn(UnstableApi::class)
@Composable
fun PlayerView(
    currentPosition: Long,
    length: Long,
    modifier: Modifier = Modifier,
    player: Player,
    isInSheet: Boolean = true,
    playerState: PlayerState,
) {
    var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var controllerVisible by remember { mutableStateOf(true) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> lifecycle = event }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

//    if (isInSheet) {
//        LaunchedEffect(controllerVisible) {
//            delay(3000L)
//            controllerVisible = false
//        }
//    }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .noRippleClickable { controllerVisible = true }
    ) {
        AndroidView(
            modifier = modifier.aspectRatio(16f / 9f),
            factory = { context ->
                PlayerView(context).also {
                    it.player = player
                    it.useController = false
                    it.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }

                    else -> Unit
                }
            },
        )
//        if (isInSheet) {
//            PlayerController(
//                controllerVisible = controllerVisible,
//                currentPosition = currentPosition,
//                length = length,
//                player = player,
//                playerState = playerState,
//            )
//        }
    }
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.PlayerController(
    controllerVisible: Boolean,
    currentPosition: Long,
    length: Long,
    player: Player,
    playerState: PlayerState,
) {
    var isSliderHeld by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(0f) }
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(currentPosition, isSliderHeld) {
        if (!isSliderHeld) progress = currentPosition.toFloat() / length
    }

    AnimatedVisibility(
        modifier = Modifier.matchParentSize(),
        visible = controllerVisible,
        exit = fadeOut(),
        enter = fadeIn(),
    ) {
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )
        Box(
            modifier = Modifier
                .width(20.dp)
                .aspectRatio(1f)
                .align(Alignment.Center)
        ) {
            when (playerState.playingStatus) {
                PlayingStatus.PLAYING -> {
                    Icon(
                        painter = painterResource(R.drawable.pause),
                        contentDescription = "pause",
                        modifier = Modifier
                            .noRippleClickable { player.pause() }
                    )
                }

                PlayingStatus.PAUSED -> {
                    Icon(
                        painter = painterResource(R.drawable.play),
                        contentDescription = "play",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .noRippleClickable { player.pause() }
                    )

                }

                else -> Unit
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            Slider(
                value = animatedProgress,
                onValueChange = {
                    isSliderHeld = true
                    sliderValue = it
                },
                onValueChangeFinished = {
                    isSliderHeld = false
                    player.seekTo((sliderValue * length).toLong())
                },
            )
            Row {

            }
        }
    }
}
