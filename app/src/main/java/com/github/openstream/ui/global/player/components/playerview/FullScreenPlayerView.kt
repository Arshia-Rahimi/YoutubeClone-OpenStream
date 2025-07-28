package com.github.openstream.ui.global.player.components.playerview

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.github.openstream.core.common.compose.ChangeOrientationOnBackButton
import com.github.openstream.core.common.compose.Orientation

@Composable
fun FullScreenPlayerView() {
    val context = LocalContext.current
    val window = (context as? Activity)?.window
    val view = LocalView.current
    
    SideEffect {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            WindowInsetsControllerCompat(it, view).apply {
                hide(WindowInsetsCompat.Type.statusBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
    
    ChangeOrientationOnBackButton(Orientation.Portrait)
    
    PlayerView(Modifier.fillMaxSize())
}
