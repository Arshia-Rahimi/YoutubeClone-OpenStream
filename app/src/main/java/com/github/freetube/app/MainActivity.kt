package com.github.freetube.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.freetube.app.navigation.TabNavigation
import com.github.freetube.core.common.util.onFirst
import com.github.freetube.core.data.LibreTubeDataRepository
import com.github.freetube.core.extractor.OkHttpDownloader
import com.github.freetube.ui.designsystem.theme.FreeTubeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.localization.Localization

class MainActivity : ComponentActivity() {

    private val downloader = OkHttpDownloader.init(null)
    private val libreTubeDataRepository by inject<LibreTubeDataRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                libreTubeDataRepository.data
                    .onFirst { loadNewPipeConfig() }
                    .collect {}
            }
        }

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
