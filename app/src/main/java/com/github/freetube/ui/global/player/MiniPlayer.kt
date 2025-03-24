package com.github.freetube.ui.global.player

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    val video by screenModel.currentVideo.collectAsStateWithLifecycle()
    val currentPosition by screenModel.currentPosition.collectAsStateWithLifecycle()

    Row(
        modifier = modifier
            .padding(bottom = 12.dp)
            .padding(horizontal = 20.dp)
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
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
                    video = video,
                    togglePlay = { screenModel.togglePlay() },
                    currentPosition = currentPosition,
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
) {
    PlayerView(
        modifier = Modifier.fillMaxHeight(),
        player = player,
    )
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start,
    ) {
        playerState?.let {
            Text(
                text = video?.name ?: "",
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
                    text = video?.length?.toTime() + " / ",
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
            onClick = togglePlay,
        ) {
            Icon(
                painter = painterResource(if (playerState?.playingStatus == PlayingStatus.PAUSED) R.drawable.play else R.drawable.pause),
                contentDescription = "exit",
            )
        }
    }
    IconButton(
        onClick = {
            // todo 
        },
    ) {
        Icon(
            painter = painterResource(R.drawable.exit),
            contentDescription = "exit",
        )
    }
}
