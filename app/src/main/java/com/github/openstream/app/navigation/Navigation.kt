package com.github.openstream.app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.openstream.app.navigation.NavigationViewModel.Companion.tabsList
import com.github.openstream.app.navigation.routes.Tabs
import com.github.openstream.core.common.compose.getCurrentRouteClassName
import com.github.openstream.ui.feature.downloads.DownloadsScreen
import com.github.openstream.ui.feature.library.navigation.LibraryNavHost
import com.github.openstream.ui.feature.search.navigation.SearchNavHost
import com.github.openstream.ui.feature.settings.SettingsScreen
import com.github.openstream.ui.feature.subscriptions.SubscriptionsScreen
import com.github.openstream.ui.global.OpenStreamScaffold
import com.github.openstream.ui.global.player.PlayerViewModel
import com.github.openstream.ui.global.player.view.FullScreenPlayerView
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
    val topBar by navigationViewModel.topBar.collectAsStateWithLifecycle()
    val shouldShowFullscreenPlayer by playerViewModel.shouldShowFullscreenPlayer.collectAsStateWithLifecycle()
    
    if (shouldShowFullscreenPlayer) FullScreenPlayerView()
    else {
        OpenStreamScaffold(
            currentTab = currentTab,
            topBar = topBar,
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
        ) {
            NavHost(
                navController = rootNavController,
                startDestination = Tabs.Subscriptions,
            ) {
                composableWithTabAnimation<Tabs.Search> {
                    SearchNavHost(
                        topBar = navigationViewModel::setTopBar,
                        playVideo = playerViewModel::start,
                    )
                }
                composableWithTabAnimation<Tabs.Library> {
                    LibraryNavHost(
                        topBar = navigationViewModel::setTopBar,
                        playVideo = playerViewModel::start,
                    )
                }
                composableWithTabAnimation<Tabs.Subscriptions> {
                    SubscriptionsScreen(
                        topBar = navigationViewModel::setTopBar,
                    )
                }
                composableWithTabAnimation<Tabs.Downloads> {
                    DownloadsScreen(
                        topBar = navigationViewModel::setTopBar,
                    )
                }
                composableWithTabAnimation<Tabs.Settings> {
                    SettingsScreen(
                        topBar = navigationViewModel::setTopBar,
                    )
                }
            }
        }
    }
}

inline fun <reified T : Any> NavGraphBuilder.composableWithTabAnimation(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(300),
            )
        },
    ) {
        content(it)
    }
}

fun NavController.isInTabRoot() = getCurrentRouteClassName()?.let { it == "Root" } != false
