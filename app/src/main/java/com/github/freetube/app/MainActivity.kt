package com.github.freetube.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.retainedComponent
import com.github.freetube.app.rootcomponent.LibreTubeRootComponent
import com.github.freetube.app.rootcomponent.TopLevelDestinations
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import com.github.freetube.ui.designsystem.theme.FreeTubeTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val root = retainedComponent { LibreTubeRootComponent(it, lifecycleScope) }

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                root.libreTubeData.value == null
            }
        }
        
        setContent {
            KoinAndroidContext {
                val libreTubeData by root.libreTubeData.collectAsStateWithLifecycle()
                var currentTLD by remember { mutableStateOf(TopLevelDestinations.Subscriptions) }
                libreTubeData?.let {
                    FreeTubeTheme(it.appTheme) {
                        LibreTubeScaffold(
                            currentTLD = currentTLD,
                        ) {
                            ChildPages(
                                pages = root.pages,
                                onPageSelected = {
                                    currentTLD = TopLevelDestinations.entries[it - 1]
                                },
                            ) { _, _ -> }
                        }
                    }
                }
            }
        }
    }
}
