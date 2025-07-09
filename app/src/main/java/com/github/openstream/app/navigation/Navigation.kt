package com.github.openstream.app.navigation

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
import com.github.openstream.app.navigation.NavigationViewModel.Companion.tabsList
import com.github.openstream.app.navigation.routes.Tabs
import com.github.openstream.core.common.compose.ObserveForEvents
import com.github.openstream.core.common.compose.getCurrentRouteClassName
import com.github.openstream.ui.feature.library.LibraryNavHost
import com.github.openstream.ui.feature.search.SearchNavHost
import com.github.openstream.ui.feature.settings.SettingsScreen
import com.github.openstream.ui.feature.subscriptions.SubscriptionsNavHost
import com.github.openstream.ui.global.OpenStreamScaffold
import com.github.openstream.ui.global.player.PlayerController
import com.github.openstream.ui.global.player.PlayerViewModel
import com.github.openstream.ui.global.player.components.playerview.FullScreenPlayerView
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
    
    ObserveForEvents(PlayerController.events) {
        playerViewModel.processAction(it)
    }
    
    if (shouldShowFullscreenPlayer) FullScreenPlayerView()
    else OpenStreamScaffold(
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
                )
            }
            composable<Tabs.Library> {
                LibraryNavHost(
                    navViewModel = navigationViewModel,
                )
            }
            composable<Tabs.Subscriptions> {
                SubscriptionsNavHost(
                    navViewModel = navigationViewModel,
                )
            }
            composable<Tabs.Settings> {
                SettingsScreen()
            }
        }
    }
}

fun NavController.isInTabRoot() = getCurrentRouteClassName()?.let { it == "Root" } != false
