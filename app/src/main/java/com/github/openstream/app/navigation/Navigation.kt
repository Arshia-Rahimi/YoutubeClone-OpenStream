package com.github.openstream.app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.openstream.app.navigation.Tabs.Companion.tabsList
import com.github.openstream.core.common.compose.getCurrentRouteClassName
import com.github.openstream.ui.feature.downloads.DownloadsScreen
import com.github.openstream.ui.feature.library.LibraryScreen
import com.github.openstream.ui.feature.search.navigation.SearchNavHost
import com.github.openstream.ui.feature.settings.SettingsScreen
import com.github.openstream.ui.feature.subscriptions.SubscriptionsScreen
import com.github.openstream.ui.global.player.OpenStreamScaffold
import com.github.openstream.ui.global.player.PlayerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.koinInject

private var topBarStateFlow: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)

@Composable
fun Navigation() {
    val rootNavController = rememberNavController()
        .apply {
            addOnDestinationChangedListener { controller, _, _ ->
                (controller.getCurrentRouteClassName() ?: "Subscriptions").let { currentTab ->
                    Tabs.currentTab.value = tabsList.first { tab ->
                        currentTab == tab.toString()
                    }
                }
            }
        }

    val playerViewModel = koinInject<PlayerViewModel>()
    val currentTab by Tabs.currentTab.collectAsStateWithLifecycle()
    val topBar by topBarStateFlow.collectAsStateWithLifecycle()

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

                destination.isInTabRoot.value -> {
                    if (isDoubleClick) destination.tabRootDoubleClickAction?.invoke()
                    else destination.isInTabRootAction?.invoke()
                }

                else -> destination.navigateToCurrentTabRoot?.invoke()
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
                    topBar = { topBarStateFlow.value = it },
                    playVideo = playerViewModel::start,
                )
            }
            composableWithTabAnimation<Tabs.Library> {
                LibraryScreen(
                    topBar = { topBarStateFlow.value = it },
                )
            }
            composableWithTabAnimation<Tabs.Subscriptions> {
                SubscriptionsScreen(
                    topBar = { topBarStateFlow.value = it },
                )
            }
            composableWithTabAnimation<Tabs.Downloads> {
                DownloadsScreen(
                    topBar = { topBarStateFlow.value = it },
                )
            }
            composableWithTabAnimation<Tabs.Settings> {
                SettingsScreen(
                    topBar = { topBarStateFlow.value = it },
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
