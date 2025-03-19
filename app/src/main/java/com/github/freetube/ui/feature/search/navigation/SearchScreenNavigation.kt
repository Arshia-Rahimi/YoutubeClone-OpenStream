package com.github.freetube.ui.feature.search.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.designsystem.sharedscreens.ChannelScreen
import com.github.freetube.ui.designsystem.sharedscreens.PlaylistScreen
import com.github.freetube.ui.feature.search.main.SearchScreen

@Composable
fun SearchScreenNavigation(
    navController: NavHostController,
) {
    NavHost(
        startDestination = LibreTubeRoutes.Search.Main,
        navController = navController,
        route = LibreTubeRoutes.Search::class,
    ) {
        composable<LibreTubeRoutes.Search.Main> {
            SearchScreen()
        }
        composable<LibreTubeRoutes.Search.Channel> {
            ChannelScreen()
        }
        composable<LibreTubeRoutes.Search.Playlist> {
            PlaylistScreen()
        }
    }
}
