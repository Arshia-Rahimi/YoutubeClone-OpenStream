package com.github.freetube.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.app.navigation.LibreTubeNavigation
import com.github.freetube.ui.designsystem.theme.FreeTubeTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    
    private val viewModel by inject<MainActivityViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.libreTubeData.value == null
            }
        }
        
        setContent {
            KoinAndroidContext {
                val libreTubeData by viewModel.libreTubeData.collectAsStateWithLifecycle()
                libreTubeData?.let {
                    FreeTubeTheme(it.appTheme) {
                        LibreTubeNavigation(it)
                    }
                }
            }
        }
    }
}
