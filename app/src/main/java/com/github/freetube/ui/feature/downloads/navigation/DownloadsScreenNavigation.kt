package com.github.freetube.ui.feature.downloads.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.downloads.DownloadsScreen

fun NavController.navigationToDownloadsScreen(navOptions: NavOptions) = 
    navigate(route = LibreTubeRoutes.Downloads, navOptions = navOptions)

fun NavGraphBuilder.downloadsScreenNavigation() {
    composable<LibreTubeRoutes.Downloads> {
        DownloadsScreen()
    }
}
