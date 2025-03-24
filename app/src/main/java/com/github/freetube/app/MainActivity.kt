package com.github.freetube.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.freetube.app.navigation.TabNavigation
import com.github.freetube.core.extractor.OkHttpDownloader
import com.github.freetube.ui.designsystem.theme.FreeTubeTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.localization.Localization

class MainActivity : ComponentActivity() {

    private val downloader = OkHttpDownloader.init(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        loadNewPipeConfig()

        setContent {
            KoinAndroidContext {
                FreeTubeTheme {
                    TabNavigation()
                }
            }
        }
    }

    private fun loadNewPipeConfig() =
        // todo get locale and add it to dataStore
        NewPipe.init(downloader, Localization("en", "US"))
}
