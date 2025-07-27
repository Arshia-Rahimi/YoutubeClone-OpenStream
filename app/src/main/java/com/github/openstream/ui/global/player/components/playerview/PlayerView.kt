package com.github.openstream.ui.global.player.components.playerview

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.ui.global.player.PlayerAction

@OptIn(UnstableApi::class)
@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    player: Player,
    isSheetExpanded: Boolean,
    isAudioModeEnabled: Boolean,
    videoThumbnail: String,
    isFullScreen: Boolean,
) {

    if(isAudioModeEnabled) {
        AsyncImage(
            model = videoThumbnail,
            contentDescription = "thumbnail",
            modifier = modifier,
        )
        Icon(
            painter = painterResource(R.drawable.play),
            contentDescription = "play",
            tint = Color.White,
            modifier = modifier.clickable { PlayerAction.ToggleAudioOnlyMode.send() },
        )
    } else {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                PlayerView(context).also {
                    it.player = player
                    it.useController = false
                    it.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                    it.setFullscreenButtonState(isFullScreen)
                    it.setFullscreenButtonClickListener {
                        (context as Activity).requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                }
            },
            update = {
                it.useController = isSheetExpanded
            },
        )
    }
}
