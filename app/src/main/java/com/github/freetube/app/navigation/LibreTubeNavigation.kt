package com.github.freetube.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.freetube.app.rememberLibreTubeAppState
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import com.github.freetube.ui.feature.downloads.navigation.DownloadsScreenNavigation
import com.github.freetube.ui.feature.library.navigation.LibraryScreenNavigation
import com.github.freetube.ui.feature.search.navigation.SearchScreenNavigation
import com.github.freetube.ui.feature.settings.navigation.SettingsScreenNavigation
import com.github.freetube.ui.feature.subscriptions.navigation.SubscriptionsScreenNavigation
import com.github.freetube.ui.global.player.MiniPlayer
import com.github.freetube.ui.global.player.PlayerSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibreTubeNavigation() {
    val appState = rememberLibreTubeAppState()
    val currentTLD by appState.currentTLD.collectAsStateWithLifecycle()
    val navController = appState.baseNavController
    var showBottomSheet by remember { mutableStateOf(false) }

    LibreTubeScaffold(
        appState = appState,
        currentTLD = currentTLD,
    ) { modifier ->
        Box(
            modifier = modifier,
        ) {
            NavHost(
                navController = navController,
                startDestination = LibreTubeRoutes.Subscriptions,
                route = LibreTubeRoutes::class,
            ) {
                composable<LibreTubeRoutes.Subscriptions>(
                ) {
                    val subscriptionsNavController = rememberNavController()
                    SubscriptionsScreenNavigation(subscriptionsNavController)
                }
                composable<LibreTubeRoutes.Settings> {
                    val settingsNavController = rememberNavController()
                    SettingsScreenNavigation(settingsNavController)
                }
                composable<LibreTubeRoutes.Library> {
                    val libraryNavController = rememberNavController()
                    LibraryScreenNavigation(libraryNavController)
                }
                composable<LibreTubeRoutes.Downloads> {
                    val downloadsNavController = rememberNavController()
                    DownloadsScreenNavigation(downloadsNavController)
                }
                composable<LibreTubeRoutes.Search> {
                    val searchNavController = rememberNavController()
                    SearchScreenNavigation(searchNavController)
                }
            }
//            NavHost(
//                modifier = Modifier.fillMaxSize(),
//                navController = navController,
//                startDestination = LibreTubeRoutes.Subscriptions
//            ) {
//                subscriptionsScreenNavigation(
//                    toSearchScreen = { appState.navigateToTopLevelDestination(TopLevelDestinations.Search) },
//                    toSubsPlaylistScreen = { navController.navigate(LibreTubeRoutes.Subscriptions.Playlist) }
//                )
//                settingsScreenNavigation()
//                libraryScreenNavigation()
//                searchScreenNavigation(
//                    navController = navController,
//                    toSubsScreen = { appState.navigateToTopLevelDestination(TopLevelDestinations.Subscriptions) },
//                    toSubsChannelScreen = { navController.navigate(LibreTubeRoutes.Subscriptions.Channel) }
//                )
//                downloadsScreenNavigation()
//            }
            MiniPlayer(
                modifier = Modifier.align(Alignment.BottomCenter),
                showBottomSheet = { showBottomSheet = true }
            )
        }
        if(showBottomSheet) PlayerSheet()
    }
}
