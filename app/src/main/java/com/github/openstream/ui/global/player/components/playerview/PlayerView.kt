package com.github.openstream.ui.global.player.components.playerview

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
    var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> lifecycle = event }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

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
    }
}
