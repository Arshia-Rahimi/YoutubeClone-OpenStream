package com.github.freetube.ui.feature.downloads.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.downloads.DownloadsScreen

@Composable
fun DownloadsScreenNavigation(
    navController: NavHostController,
) {
    NavHost(
        startDestination = LibreTubeRoutes.Downloads.Main,
        navController = navController,
        route = LibreTubeRoutes.Downloads::class,
    ) {
        composable<LibreTubeRoutes.Downloads.Main> {
            DownloadsScreen()
        }
    }
}
