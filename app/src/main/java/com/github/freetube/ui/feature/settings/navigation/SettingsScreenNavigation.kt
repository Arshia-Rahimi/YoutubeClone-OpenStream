package com.github.freetube.ui.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.settings.SettingsScreen

fun NavGraphBuilder.settingsScreenNavigation(
    toHomeScreen: () -> Unit,
) {
    composable<LibreTubeRoutes.Settings> {
        SettingsScreen(
            toHomeScreen = toHomeScreen,
        )
    }
}
