package com.github.freetube.app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.freetube.core.common.compose.getCurrentRouteClassName
import com.github.freetube.ui.feature.downloads.DownloadsScreen
import com.github.freetube.ui.feature.library.LibraryScreen
import com.github.freetube.ui.feature.search.navigation.SearchNavHost
import com.github.freetube.ui.feature.settings.SettingsScreen
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreen
import com.github.freetube.ui.global.player.LibreTubeScaffold
import com.github.freetube.ui.global.player.PlayerViewModel
import org.koin.compose.koinInject

@Composable
fun Navigation() {
    val rootNavController = rememberNavController()
    val playerViewModel = koinInject<PlayerViewModel>()
    val currentTab = rootNavController.currentBackStackEntryAsState()
        .value?.destination?.route?.split(".")?.last() ?: Tabs.Subscriptions.toString()
    val playVideo: (String) -> Unit = { playerViewModel.start(it) }
    var topBar by remember { mutableStateOf<(@Composable () -> Unit)?>(null) }

    LibreTubeScaffold(
        currentTab = currentTab,
        topBar = topBar,
        navigateToTab = { destination ->
            when (rootNavController.getCurrentRouteClassName()) {
                null -> Unit
                destination.toString() -> destination.navigateToRoot?.invoke()
                else -> {
                    rootNavController.navigate(destination) {
                        popUpTo(rootNavController.graph.findStartDestination().id) {
                            saveState = true
                            inclusive = false
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
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
                    topBar = { topBar = it },
                    playVideo = playVideo,
                )
            }
            composableWithTabAnimation<Tabs.Library> {
                LibraryScreen(
                    topBar = { topBar = it },
                )
            }
            composableWithTabAnimation<Tabs.Subscriptions> {
                SubscriptionsScreen(
                    topBar = { topBar = it },
                )
            }
            composableWithTabAnimation<Tabs.Downloads> {
                DownloadsScreen(
                    topBar = { topBar = it },
                )
            }
            composableWithTabAnimation<Tabs.Settings> {
                SettingsScreen(
                    topBar = { topBar = it },
                )
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
