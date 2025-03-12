package com.github.freetube.ui.feature.playlists.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.playlists.PlaylistsScreen

fun NavController.navigateToPlaylists(navOptions: NavOptions) =
    navigate(route = LibreTubeRoutes.Playlists, navOptions =  navOptions)

fun NavGraphBuilder.playlistsScreenNavigation() {
   composable<LibreTubeRoutes.Playlists> {
       PlaylistsScreen()
   } 
}
