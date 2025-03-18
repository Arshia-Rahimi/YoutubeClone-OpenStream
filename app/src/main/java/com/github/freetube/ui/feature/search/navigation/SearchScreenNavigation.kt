package com.github.freetube.ui.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.app.navigation.topLevelScreenComposable
import com.github.freetube.ui.designsystem.sharedscreens.ChannelScreen
import com.github.freetube.ui.designsystem.sharedscreens.PlaylistScreen
import com.github.freetube.ui.feature.search.main.SearchScreen

fun NavController.navigateToSearchScreen(navOptions: NavOptions) =
    navigate(route = LibreTubeRoutes.Search, navOptions =  navOptions)

fun NavGraphBuilder.searchScreenNavigation(
    toSubsScreen: () -> Unit,
    toSubsChannelScreen: () -> Unit,
) {
    navigation<LibreTubeRoutes.Search>(
        startDestination = LibreTubeRoutes.Search.Main,
    ) {
        topLevelScreenComposable<LibreTubeRoutes.Search.Main> {
            SearchScreen()
        }
        composable<LibreTubeRoutes.Subscriptions.Channel> {
            ChannelScreen("subs", "to subsplaylist") { toSubsChannelScreen() }
        }
        composable<LibreTubeRoutes.Search.Playlist> {
            PlaylistScreen("subs", "to Search") { toSubsScreen() }
        }
    }
}
