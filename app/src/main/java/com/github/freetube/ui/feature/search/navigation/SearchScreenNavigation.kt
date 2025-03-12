package com.github.freetube.ui.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.search.SearchScreen

fun NavGraphBuilder.searchScreenNavigation() {
    composable<LibreTubeRoutes.Search> {
        SearchScreen()
    }
}
