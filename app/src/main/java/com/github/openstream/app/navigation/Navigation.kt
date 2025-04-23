package com.github.openstream.app.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.openstream.ui.feature.downloads.DownloadsScreen
import com.github.openstream.ui.feature.library.LibraryScreen
import com.github.openstream.ui.feature.search.navigation.SearchNavHost
import com.github.openstream.ui.feature.settings.SettingsScreen
import com.github.openstream.ui.feature.subscriptions.SubscriptionsScreen
import com.github.openstream.ui.global.OpenStreamScaffold
import com.github.openstream.ui.global.player.PlayerViewModel
import com.github.openstream.ui.global.player.view.FullScreenPlayerView
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.koinInject

private var topBarStateFlow: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation() {
//    val rootNavController = rememberNavController()
//        .apply {
//            addOnDestinationChangedListener { controller, _, _ ->
//                (controller.getCurrentRouteClassName() ?: "Subscriptions").let { currentTab ->
//                    Tabs.currentTab.value = tabsList.first { tab ->
//                        currentTab == tab.toString()
//                    }
//                }
//            }
//        }

    val playerViewModel = koinInject<PlayerViewModel>()
    val currentTab by Tabs.currentTab.collectAsStateWithLifecycle()
    val topBar by topBarStateFlow.collectAsStateWithLifecycle()
    val shouldShowFullscreenPlayer by playerViewModel.shouldShowFullscreenPlayer.collectAsStateWithLifecycle()

    if (shouldShowFullscreenPlayer) FullScreenPlayerView()
    else {
        OpenStreamScaffold(
            currentTab = currentTab,
            topBar = topBar,
            navAction = { destination, isDoubleClick ->
                when {
                    currentTab != destination -> {
//                    rootNavController.navigate(destination) {
//                        popUpTo(rootNavController.graph.findStartDestination().id) {
//                            saveState = true
//                            inclusive = false
//                        }
//                        restoreState = true
//                        launchSingleTop = true
//                    }
                        Tabs.currentTab.value = destination
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
//        NavHost(
//            navController = rootNavController,
//            startDestination = Tabs.Subscriptions,
//        ) {
//            composableWithTabAnimation<Tabs.Search> {
//                SearchNavHost(
//                    topBar = { topBarStateFlow.value = it },
//                    playVideo = playerViewModel::start,
//                )
//            }
//            composableWithTabAnimation<Tabs.Library> {
//                LibraryScreen(
//                    topBar = { topBarStateFlow.value = it },
//                )
//            }
//            composableWithTabAnimation<Tabs.Subscriptions> {
//                SubscriptionsScreen(
//                    topBar = { topBarStateFlow.value = it },
//                )
//            }
//            composableWithTabAnimation<Tabs.Downloads> {
//                DownloadsScreen(
//                    topBar = { topBarStateFlow.value = it },
//                )
//            }
//            composableWithTabAnimation<Tabs.Settings> {
//                SettingsScreen(
//                    topBar = { topBarStateFlow.value = it },
//                )
//            }
//        }
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(300),
                    ) togetherWith fadeOut(tween(300))
                }
            ) {
                when (it) {
                    Tabs.Search -> SearchNavHost(
                        topBar = { topBarStateFlow.value = it },
                        playVideo = playerViewModel::start,
                    )

                    Tabs.Library -> LibraryScreen(
                        topBar = { topBarStateFlow.value = it },
                    )

                    Tabs.Subscriptions -> SubscriptionsScreen(
                        topBar = { topBarStateFlow.value = it },
                    )

                    Tabs.Downloads -> DownloadsScreen(
                        topBar = { topBarStateFlow.value = it },
                    )

                    Tabs.Settings -> SettingsScreen(
                        topBar = { topBarStateFlow.value = it },
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
