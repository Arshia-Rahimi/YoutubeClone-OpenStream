package com.github.freetube.ui.feature.library.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.library.LibraryScreen

fun NavController.navigateToLibrary(navOptions: NavOptions) =
    navigate(route = LibreTubeRoutes.Library, navOptions = navOptions)

fun NavGraphBuilder.libraryScreenNavigation() {
    navigation<LibreTubeRoutes.Library>(
        startDestination = LibreTubeRoutes.Library.Main
    ) {
        composable<LibreTubeRoutes.Library.Main> {
            LibraryScreen()
        }
    }
}
