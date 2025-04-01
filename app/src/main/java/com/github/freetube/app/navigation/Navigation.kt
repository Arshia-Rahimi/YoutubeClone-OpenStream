package com.github.freetube.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.github.freetube.ui.feature.downloads.DownloadsScreen
import com.github.freetube.ui.feature.library.LibraryScreen
import com.github.freetube.ui.feature.search.SearchScreen
import com.github.freetube.ui.feature.settings.SettingsScreen
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreen
import com.github.freetube.ui.global.channel.ChannelScreen
import com.github.freetube.ui.global.player.LibreTubeScaffold
import com.github.freetube.ui.global.player.PlayerViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val playerViewModel = koinInject<PlayerViewModel>()
    val topBar: (@Composable () -> Unit) -> Unit = { playerViewModel.topBar.value = it }
    val playVideo: (String) -> Unit = { playerViewModel.start(it) }
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = false,
        )
    )
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value?.destination

    LibreTubeScaffold(
        sheetScaffoldState = sheetScaffoldState,
        currentEntry = currentBackStackEntry,
        navigateToTab = {
            navController.navigate(it) {
                popUpTo(it) {
                    saveState = true
                    inclusive = false
                }
                restoreState = true
                launchSingleTop = true
            }
        },
    ) { modifier ->
        Box(modifier) {
            NavHost(
                navController = navController,
                startDestination = SubscriptionsRoute
            ) {
                navigation(
                    startDestination = SearchRoute.Main,
                    route = SearchRoute::class,
                ) {
                    composable<SearchRoute.Main> {
                        SearchScreen(
                            topBar = topBar,
                            toChannelScreen = { navController.navigate(SearchRoute.Channel(it)) },
                            toPlaylistScreen = { navController.navigate(SearchRoute.Playlist(it)) },
                            playVideo = playVideo,
                        )
                    }
                    composable<SearchRoute.Channel> {
                        ChannelScreen(
                            url = "",
                            topBar = topBar,
                            playVideo = playVideo,
                            navigateBack = { navController.popBackStack() },
                            toPlaylistScreen = { navController.navigate(SearchRoute.Playlist(it)) },
                        )
                    }
                }
                navigation(
                    startDestination = LibraryRoute.Main,
                    route = LibraryRoute::class,
                ) {
                    composable<LibraryRoute.Main> {
                        LibraryScreen()
                    }
                }
                navigation(
                    startDestination = SubscriptionsRoute.Main,
                    route = SubscriptionsRoute::class,
                ) {
                    composable<SubscriptionsRoute.Main> {
                        SubscriptionsScreen()
                    }
                }
                navigation(
                    startDestination = DownloadsRoute.Main,
                    route = DownloadsRoute::class,
                ) {
                    composable<DownloadsRoute.Main> {
                        DownloadsScreen()
                    }
                }
                navigation(
                    startDestination = SettingsRoute.Main,
                    route = SettingsRoute::class,
                ) {
                    composable<SettingsRoute.Main> {
                        SettingsScreen()
                    }
                }
            }
        }
    }
}
