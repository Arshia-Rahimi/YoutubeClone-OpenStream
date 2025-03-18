package com.github.freetube.ui.feature.subscriptions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.app.navigation.topLevelScreenComposable
import com.github.freetube.ui.designsystem.sharedscreens.ChannelScreen
import com.github.freetube.ui.designsystem.sharedscreens.PlaylistScreen
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreen

fun NavController.navigateToSubscriptions(navOptions: NavOptions) =
    navigate(route = LibreTubeRoutes.Subscriptions, navOptions = navOptions)

fun NavGraphBuilder.subscriptionsScreenNavigation(
    toSubsPlaylistScreen: () -> Unit,
    toSearchScreen: () -> Unit,
) {
    navigation<LibreTubeRoutes.Subscriptions>(
        startDestination = LibreTubeRoutes.Subscriptions.Main,
    ) {
        topLevelScreenComposable<LibreTubeRoutes.Subscriptions.Main> {
            SubscriptionsScreen(toSubsPlaylistScreen)
        }
        composable<LibreTubeRoutes.Subscriptions.Channel> {
            ChannelScreen("subs", "to subsplaylist") { toSubsPlaylistScreen() }
        }
        composable<LibreTubeRoutes.Subscriptions.Playlist> {
            PlaylistScreen("subs", "to Search") { toSearchScreen() }
        }
    }
}
