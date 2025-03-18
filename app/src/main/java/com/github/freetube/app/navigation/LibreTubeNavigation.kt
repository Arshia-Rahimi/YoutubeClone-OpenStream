package com.github.freetube.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.github.freetube.app.rememberLibreTubeAppState
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import com.github.freetube.ui.feature.downloads.navigation.downloadsScreenNavigation
import com.github.freetube.ui.feature.library.navigation.libraryScreenNavigation
import com.github.freetube.ui.feature.search.navigation.searchScreenNavigation
import com.github.freetube.ui.feature.settings.navigation.settingsScreenNavigation
import com.github.freetube.ui.feature.subscriptions.navigation.subscriptionsScreenNavigation
import com.github.freetube.ui.global.player.MiniPlayer
import com.github.freetube.ui.global.player.PlayerSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibreTubeNavigation() {
    val appState = rememberLibreTubeAppState()
    val navController = appState.navController
    var showBottomSheet by remember { mutableStateOf(false) }

    LibreTubeScaffold(
        appState = appState,
    ) { modifier ->
        Box(
            modifier = modifier,
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = LibreTubeRoutes.Subscriptions
            ) {
                subscriptionsScreenNavigation(
                    toSearchScreen = { appState.navigateToTopLevelDestination(TopLevelDestinations.Search) },
                    toSubsPlaylistScreen = { navController.navigate(LibreTubeRoutes.Subscriptions.Playlist) }
                )
                settingsScreenNavigation()
                libraryScreenNavigation()
                searchScreenNavigation(
                    toSubsScreen = { appState.navigateToTopLevelDestination(TopLevelDestinations.Subscriptions) },
                    toSubsChannelScreen = { navController.navigate(LibreTubeRoutes.Subscriptions.Channel) }
                )
                downloadsScreenNavigation()
            }
            MiniPlayer(
                modifier = Modifier.align(Alignment.BottomCenter),
                showBottomSheet = { showBottomSheet = true }
            )
        }
        if(showBottomSheet) PlayerSheet()
    }
}
