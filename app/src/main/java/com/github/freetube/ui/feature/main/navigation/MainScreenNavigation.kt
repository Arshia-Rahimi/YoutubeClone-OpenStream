package com.github.freetube.ui.feature.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.main.MainScreen

fun NavGraphBuilder.mainScreenNavigation(
    toSettingsScreen: () -> Unit,
) {
    composable<LibreTubeRoutes.MainRoute> {
        MainScreen(
            toSettingsScreen = toSettingsScreen
        )
    }
}
