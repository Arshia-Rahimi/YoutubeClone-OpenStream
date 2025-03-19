package com.github.freetube.ui.feature.library.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.designsystem.sharedscreens.ChannelScreen
import com.github.freetube.ui.designsystem.sharedscreens.PlaylistScreen
import com.github.freetube.ui.feature.library.LibraryScreen

@Composable
fun LibraryScreenNavigation(
    navController: NavHostController,
) {
    NavHost(
        startDestination = LibreTubeRoutes.Library.Main,
        navController = navController,
        route = LibreTubeRoutes.Library::class,
    ) {
        composable<LibreTubeRoutes.Library.Main> {
            LibraryScreen()
        }
        composable<LibreTubeRoutes.Library.Channel> {
            ChannelScreen()
        }
        composable<LibreTubeRoutes.Library.Playlist> {
            PlaylistScreen()
        }
    }
}
