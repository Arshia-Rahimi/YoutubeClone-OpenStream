package com.github.openstream.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.github.openstream.app.navigation.Navigation
import com.github.openstream.core.extractor.OkHttpDownloader
import com.github.openstream.ui.designsystem.theme.OpenStreamTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.localization.Localization

class MainActivity : ComponentActivity() {

    private val downloader = OkHttpDownloader.init(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false
        
        loadNewPipeConfig()

        setContent {
            KoinAndroidContext {
                OpenStreamTheme {
                    Navigation()
                }
            }
        }
    }

    private fun loadNewPipeConfig() =
        // todo get locale and add it to dataStore
        NewPipe.init(downloader, Localization("en", "US"))
}
