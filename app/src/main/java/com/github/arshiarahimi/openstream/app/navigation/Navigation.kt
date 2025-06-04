package com.github.arshiarahimi.openstream.app.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.arshiarahimi.openstream.app.navigation.NavigationViewModel.Companion.tabsList
import com.github.arshiarahimi.openstream.app.navigation.routes.Tabs
import com.github.arshiarahimi.openstream.core.common.compose.getCurrentRouteClassName
import com.github.arshiarahimi.openstream.ui.feature.downloads.QueueScreen
import com.github.arshiarahimi.openstream.ui.feature.library.navigation.LibraryNavHost
import com.github.arshiarahimi.openstream.ui.feature.search.navigation.SearchNavHost
import com.github.arshiarahimi.openstream.ui.feature.settings.SettingsScreen
import com.github.arshiarahimi.openstream.ui.feature.subscriptions.navigation.SubscriptionsNavHost
import com.github.arshiarahimi.openstream.ui.global.OpenStreamScaffold
import com.github.arshiarahimi.openstream.ui.global.player.PlayerViewModel
import com.github.arshiarahimi.openstream.ui.global.player.view.FullScreenPlayerView
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun Navigation() {
    val navigationViewModel = koinViewModel<NavigationViewModel>()
    val playerViewModel = koinInject<PlayerViewModel>()

    val rootNavController = rememberNavController()
        .apply {
            addOnDestinationChangedListener { controller, _, _ ->
                (controller.getCurrentRouteClassName() ?: "Subscriptions").let { currentTab ->
                    navigationViewModel.currentTab.value = tabsList.first { tab ->
                        currentTab == tab.toString()
                    }
                }
            }
        }

    val currentTab by navigationViewModel.currentTab.collectAsStateWithLifecycle()
    val shouldShowFullscreenPlayer by playerViewModel.shouldShowFullscreenPlayer.collectAsStateWithLifecycle()

    if (shouldShowFullscreenPlayer) FullScreenPlayerView()
    else {
        OpenStreamScaffold(
            currentTab = currentTab,
            navAction = { destination, isDoubleClick ->
                when {
                    currentTab != destination -> {
                        rootNavController.navigate(destination) {
                            popUpTo(rootNavController.graph.findStartDestination().id) {
                                saveState = true
                                inclusive = false
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }

                    isDoubleClick -> navigationViewModel.tabDoubleClick(destination)
                    else -> navigationViewModel.tabClick(destination)
                }
            },
            toChannelScreen = {},
        ) { ip ->
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(ip)
                    .consumeWindowInsets(ip),
                navController = rootNavController,
                startDestination = Tabs.Subscriptions,
            ) {
                composable<Tabs.Search> {
                    SearchNavHost(
                        navViewModel = navigationViewModel,
                        playerViewModel = playerViewModel,
                    )
                }
                composable<Tabs.Library> {
                    LibraryNavHost(
                        navViewModel = navigationViewModel,
                        playerViewModel = playerViewModel,
                    )
                }
                composable<Tabs.Subscriptions> {
                    SubscriptionsNavHost(
                        navViewModel = navigationViewModel,
                        playerViewModel = playerViewModel,
                    )
                }
                composable<Tabs.Queue> {
                    QueueScreen()
                }
                composable<Tabs.Settings> {
                    SettingsScreen()
                }
            }
        }
    }
}

fun NavController.isInTabRoot() = getCurrentRouteClassName()?.let { it == "Root" } != false
