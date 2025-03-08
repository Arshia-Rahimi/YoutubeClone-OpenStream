package com.github.freetube.ui.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.home.HomeScreen

fun NavGraphBuilder.mainScreenNavigation(
    toSettingsScreen: () -> Unit,
) {
    composable<LibreTubeRoutes.Home> {
        HomeScreen(
            toSettingsScreen = toSettingsScreen
        )
    }
}
