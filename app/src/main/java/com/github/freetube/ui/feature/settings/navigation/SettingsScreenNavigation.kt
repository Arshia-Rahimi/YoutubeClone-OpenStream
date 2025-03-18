package com.github.freetube.ui.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.settings.SettingsScreen

fun NavController.navigateToSettings(navOptions: NavOptions) =
    navigate(route = LibreTubeRoutes.Settings, navOptions =  navOptions)

fun NavGraphBuilder.settingsScreenNavigation() {
    navigation<LibreTubeRoutes.Settings>(
        startDestination = LibreTubeRoutes.Settings.Main,
    ) {
        composable<LibreTubeRoutes.Settings.Main> {
            SettingsScreen()
        }
    }
}
