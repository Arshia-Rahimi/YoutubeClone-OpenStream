package com.github.openstream.ui.navigation

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
import com.github.openstream.ui.feature.library.navigation.LibraryNavHost
import com.github.openstream.ui.feature.search.navigation.SearchNavHost
import com.github.openstream.ui.feature.settings.SettingsScreen
import com.github.openstream.ui.feature.subscriptions.SubscriptionsScreen
import com.github.openstream.ui.global.components.player.PlayerViewModel
import com.github.openstream.ui.global.components.player.view.FullScreenPlayerView
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

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
    
    val navigationViewModel = koinViewModel<NavigationViewModel>()
    val playerViewModel = koinInject<PlayerViewModel>()
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
//                    rootNavController.navigate(destination) {
//                        popUpTo(rootNavController.graph.findStartDestination().id) {
//                            saveState = true
//                            inclusive = false
//                        }
//                        restoreState = true
//                        launchSingleTop = true
//                    }
                        navigationViewModel.setCurrentTab(destination)
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
                        topBar = navigationViewModel::setTopBar,
                        playVideo = playerViewModel::start,
                    )

                    Tabs.Library -> LibraryNavHost(
                        topBar = navigationViewModel::setTopBar,
                        playVideo = playerViewModel::start,
                    )
                    
                    Tabs.Subscriptions -> SubscriptionsScreen(
                        topBar = navigationViewModel::setTopBar,
                    )
                    
                    Tabs.Downloads -> DownloadsScreen(
                        topBar = navigationViewModel::setTopBar,
                    )
                    
                    Tabs.Settings -> SettingsScreen(
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
