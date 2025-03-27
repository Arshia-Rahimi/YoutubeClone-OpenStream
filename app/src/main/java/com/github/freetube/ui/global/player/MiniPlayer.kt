package com.github.freetube.ui.global.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import com.arshia.freetube.R
import com.github.freetube.core.common.util.toTime
import com.github.freetube.core.extractor.video.VideoData
import com.github.freetube.core.media3.PlayerState
import com.github.freetube.core.media3.PlayingStatus
import com.github.freetube.ui.global.player.components.PlayerView

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    screenModel: PlayerScreenModel,
    shouldShowMiniPlayer: Boolean,
    showBottomSheet: () -> Unit,
) {
    val uiState by screenModel.state.collectAsStateWithLifecycle()
    val playerState by screenModel.playerState.collectAsStateWithLifecycle()
    val currentPosition by screenModel.currentPosition.collectAsStateWithLifecycle()

    Row(
        modifier = modifier
            .widthIn(max = 600.dp)
            .height(60.dp)
            .fillMaxWidth()
            .background(Color(0xFF272727))
            .clickable { showBottomSheet() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (uiState) {
            is PlayerScreenModel.UiState.Loading -> CircularProgressIndicator()
            is PlayerScreenModel.UiState.Error -> Text(
                (uiState as PlayerScreenModel.UiState.Error).message ?: ""
            )

            is PlayerScreenModel.UiState.Success -> {
                MiniPlayer(
                    player = screenModel.viewPlayer,
                    playerState = playerState,
                    video = (uiState as PlayerScreenModel.UiState.Success).data,
                    togglePlay = { screenModel.togglePlay() },
                    currentPosition = currentPosition,
                    dispose = { screenModel.dispose() },
                    shouldShowMiniPlayer = shouldShowMiniPlayer,
                )
            }
        }
    }
}

@Composable
private fun RowScope.MiniPlayer(
    player: Player,
    playerState: PlayerState?,
    video: VideoData,
    shouldShowMiniPlayer: Boolean,
    currentPosition: Long,
    togglePlay: () -> Unit,
    dispose: () -> Unit,
) {
    var progress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(currentPosition) {
        progress = currentPosition.toFloat() / video.length.toFloat()
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )
    Column {
        LinearProgressIndicator(
            drawStopIndicator = {},
            gapSize = 0.dp,
            strokeCap = StrokeCap.Square,
            trackColor = Color(0xFF5D5D5D),
            color = Color(0xFFBBBBBB),
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (shouldShowMiniPlayer) {
                PlayerView(
                    player = player,
                    showController = false,
                )
            } else Box(Modifier.aspectRatio(16 / 9f))
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = video.name,
                    fontSize = 16.sp,
                    modifier = Modifier.basicMarquee(),
                    maxLines = 2,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = currentPosition.toTime() + " / ",
                        fontSize = 12.sp,
                        color = Color(0xFFAAAAAA),
                        maxLines = 1,
                    )
                    Text(
                        text = video.length.toTime(),
                        fontSize = 12.sp,
                        color = Color(0xFFAAAAAA),
                        maxLines = 1,
                    )
                }
            }
            if (playerState?.playingStatus == PlayingStatus.BUFFERING) {
                CircularProgressIndicator()
            } else {
                IconButton(
                    onClick = { togglePlay() },
                ) {
                    Icon(
                        painter = painterResource(if (playerState?.playingStatus == PlayingStatus.PAUSED) R.drawable.play else R.drawable.pause),
                        contentDescription = "exit",
                    )
                }
            }
            IconButton(
                onClick = { dispose() },
            ) {
                Icon(
                    painter = painterResource(R.drawable.exit),
                    contentDescription = "exit",
                )
            }
        }
    }
}
