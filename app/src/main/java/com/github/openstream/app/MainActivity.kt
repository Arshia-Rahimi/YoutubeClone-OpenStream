package com.github.openstream.app

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.github.openstream.core.extractor.OkHttpDownloader
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.ui.designsystem.theme.OpenStreamTheme
import com.github.openstream.ui.navigation.Navigation
import org.koin.android.ext.android.inject
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.localization.Localization

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false
        
        loadNewPipeConfig()
        
        setContent {
            OpenStreamTheme {
                Navigation()
            }
        }
    }
    
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
    
    override fun onDestroy() {
        super.onDestroy()
        val player by inject<OpenStreamMediaPlayer>()
        player.clear()
        player.destroy()
        
    }
    
    private fun loadNewPipeConfig() {
        val downloader = OkHttpDownloader.init(null)
        NewPipe.init(downloader, Localization("en", "US"))
    }
}
