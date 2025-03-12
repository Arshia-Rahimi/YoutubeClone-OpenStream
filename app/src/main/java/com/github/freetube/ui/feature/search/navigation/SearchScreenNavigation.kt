package com.github.freetube.ui.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.search.SearchScreen

fun NavController.navigateToSearch(navOptions: NavOptions) =
    navigate(route = LibreTubeRoutes.Search, navOptions =  navOptions)

fun NavGraphBuilder.searchScreenNavigation() {
    composable<LibreTubeRoutes.Search> {
        SearchScreen()
    }
}
