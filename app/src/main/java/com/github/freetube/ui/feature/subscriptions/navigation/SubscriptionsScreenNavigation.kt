package com.github.freetube.ui.feature.subscriptions.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.designsystem.sharedscreens.ChannelScreen
import com.github.freetube.ui.designsystem.sharedscreens.PlaylistScreen
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreen

@Composable
fun SubscriptionsScreenNavigation(
    navController: NavHostController,
) {
    NavHost(
        startDestination = LibreTubeRoutes.Subscriptions.Main,
        navController = navController,
        route = LibreTubeRoutes.Subscriptions::class,
    ) {
        composable<LibreTubeRoutes.Subscriptions.Main> {
            SubscriptionsScreen(
                toPlaylists = { navController.navigate(LibreTubeRoutes.Subscriptions.Playlist) }
            )
        }
        composable<LibreTubeRoutes.Subscriptions.Channel> {
            ChannelScreen()
        }
        composable<LibreTubeRoutes.Subscriptions.Playlist> {
            PlaylistScreen()
        }
    }
}
