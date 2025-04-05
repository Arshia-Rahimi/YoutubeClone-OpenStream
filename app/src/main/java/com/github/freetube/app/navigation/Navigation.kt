package com.github.freetube.app.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.freetube.ui.feature.downloads.DownloadsScreen
import com.github.freetube.ui.feature.library.LibraryScreen
import com.github.freetube.ui.feature.search.navigation.SearchNavHost
import com.github.freetube.ui.feature.settings.SettingsScreen
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreen
import com.github.freetube.ui.global.player.LibreTubeScaffold
import com.github.freetube.ui.global.player.PlayerViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val rootNavController = rememberNavController()
    val playerViewModel = koinInject<PlayerViewModel>()
    val currentTab = rootNavController.currentBackStackEntryAsState()
        .value?.destination?.route?.split(".")?.last() ?: SubscriptionsRoute.toString()
    var topBar: (@Composable () -> Unit)? by remember { mutableStateOf(null) }
    val playVideo: (String) -> Unit = { playerViewModel.start(it) }

    LibreTubeScaffold(
        currentTab = currentTab,
        topBar = topBar,
        navigateToTab = {
            rootNavController.navigate(it) {
                popUpTo(rootNavController.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = false
                }
                restoreState = true
                launchSingleTop = true
            }
        },
    ) {
        NavHost(
            navController = rootNavController,
            startDestination = SubscriptionsRoute
        ) {
            composable<SearchRoute> {
                SearchNavHost(
                    topBar = { topBar = it },
                    playVideo = playVideo,
                )
            }
            composable<LibraryRoute> {
                LibraryScreen()
            }
            composable<SubscriptionsRoute> {
                SubscriptionsScreen()
            }
            composable<DownloadsRoute> {
                DownloadsScreen()
            }
            composable<SettingsRoute> {
                SettingsScreen()
            }
        }
    }
}
