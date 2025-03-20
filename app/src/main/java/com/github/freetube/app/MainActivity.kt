package com.github.freetube.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.retainedComponent
import com.github.freetube.app.rootcomponent.LibreTubeRootComponent
import com.github.freetube.app.rootcomponent.Tabs
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import com.github.freetube.ui.designsystem.theme.FreeTubeTheme
import com.github.freetube.ui.feature.downloads.DownloadsComponent
import com.github.freetube.ui.feature.downloads.DownloadsScreen
import com.github.freetube.ui.feature.library.LibraryComponent
import com.github.freetube.ui.feature.library.LibraryScreen
import com.github.freetube.ui.feature.search.SearchComponent
import com.github.freetube.ui.feature.search.SearchScreen
import com.github.freetube.ui.feature.settings.SettingsComponent
import com.github.freetube.ui.feature.settings.SettingsScreen
import com.github.freetube.ui.feature.subscriptions.SubscriptionsComponent
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreen
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
                val pages by root.pages.subscribeAsState()
                val currentTab by remember { derivedStateOf { Tabs.entries[pages.selectedIndex] } }
                libreTubeData?.let {
                    FreeTubeTheme(it.appTheme) {
                        LibreTubeScaffold(
                            currentTab = currentTab,
                            navigateToTab = { root.selectTab(it.ordinal) },
                        ) {
                            ChildPages(
                                pages = root.pages,
                                onPageSelected = root::selectTab,
                                scrollAnimation = PagesScrollAnimation.Default,
                            ) { _, page ->
                                when (page) {
                                    is SubscriptionsComponent -> SubscriptionsScreen(page)
                                    is SearchComponent -> SearchScreen(page)
                                    is DownloadsComponent -> DownloadsScreen(page)
                                    is LibraryComponent -> LibraryScreen(page)
                                    is SettingsComponent -> SettingsScreen(page)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
