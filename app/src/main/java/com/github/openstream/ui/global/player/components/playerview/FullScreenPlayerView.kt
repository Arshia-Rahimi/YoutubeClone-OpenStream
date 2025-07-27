package com.github.openstream.ui.global.player.components.playerview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.github.openstream.ui.global.player.PlayerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(UnstableApi::class, ExperimentalFoundationApi::class)
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun FullScreenPlayerView() {
    val viewModel = koinViewModel<PlayerViewModel>()

    AndroidView(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black),
        factory = { context ->
            PlayerView(context).also {
                it.player = viewModel.playerInstance
                it.useController = true
                it.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                it.setFullscreenButtonState(true)
                it.setFullscreenButtonClickListener {
                    (context as Activity).requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
        },
    )
}
