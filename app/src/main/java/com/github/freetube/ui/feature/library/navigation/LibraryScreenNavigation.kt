package com.github.freetube.ui.feature.library.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.library.LibraryScreen

fun NavController.navigateToLibrary(navOptions: NavOptions) =
    navigate(route = LibreTubeRoutes.Library, navOptions =  navOptions)

fun NavGraphBuilder.libraryScreenNavigation() {
   composable<LibreTubeRoutes.Library> {
       LibraryScreen()
   } 
}
