package com.github.freetube.ui.feature.settings.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.settings.SettingsScreen

@Composable
fun SettingsScreenNavigation(
    navController: NavHostController,
) {
    NavHost(
        startDestination = LibreTubeRoutes.Settings.Main,
        navController = navController,
        route = LibreTubeRoutes.Settings::class,
    ) {
        composable<LibreTubeRoutes.Settings.Main> {
            SettingsScreen()
        }
    }
}
