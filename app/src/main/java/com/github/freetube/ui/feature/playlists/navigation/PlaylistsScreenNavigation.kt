package com.github.freetube.ui.feature.playlists.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.playlists.PlaylistsScreen

fun NavGraphBuilder.playlistsScreenNavigation() {
   composable<LibreTubeRoutes.Playlists> {
       PlaylistsScreen()
   } 
}
