package com.github.freetube.ui.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.settings.SettingsScreen

fun NavController.navigateToSettings(navOptions: NavOptions) =
    navigate(route = LibreTubeRoutes.Settings, navOptions =  navOptions)

fun NavGraphBuilder.settingsScreenNavigation() {
    composable<LibreTubeRoutes.Settings> {
        SettingsScreen()
    }
}
