package com.github.freetube.ui.global.player

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    showBottomSheet: () -> Unit,
) {
    val uiState by screenModel.state.collectAsStateWithLifecycle()
    val playerState by screenModel.playerState.collectAsStateWithLifecycle()
    val currentPosition by screenModel.currentPosition.collectAsStateWithLifecycle()

    Row(
        modifier = modifier
            .widthIn(max = 600.dp)
            .height(80.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
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
                )
            }
        }
    }
}

@Composable
private fun RowScope.MiniPlayer(
    player: Player,
    playerState: PlayerState?,
    video: VideoData?,
    currentPosition: Long,
    togglePlay: () -> Unit,
    dispose: () -> Unit,
) {
    PlayerView(
        player = player,
        showController = false,
    )
    Column(
        modifier = Modifier
            .padding(4.dp)
            .weight(1f),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start,
    ) {
        video?.let {
            Text(
                text = it.name,
                fontSize = 16.sp,
                modifier = Modifier.basicMarquee(),
                maxLines = 2,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = (currentPosition / 1000).toTime() + " / ",
                    fontSize = 12.sp,
                    color = Color(0xFFAAAAAA),
                    maxLines = 1,
                )
                Text(
                    text = it.length.toTime(),
                    fontSize = 12.sp,
                    color = Color(0xFFAAAAAA),
                    maxLines = 1,
                )
            }
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
