package com.github.freetube.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.github.freetube.app.LibreTubeAppState
import com.github.freetube.ui.feature.home.navigation.mainScreenNavigation
import com.github.freetube.ui.feature.settings.navigation.settingsScreenNavigation

@Composable
fun LibreTubeNavigation(
    libreTubeAppState: LibreTubeAppState,
) {
    val navController = libreTubeAppState.navController

    NavHost(
        navController = navController,
        startDestination = LibreTubeRoutes.Home
    ) {
        mainScreenNavigation(
            toSettingsScreen = { navController.navigate(LibreTubeRoutes.Settings) }
        )
        settingsScreenNavigation(
            toHomeScreen = { navController.navigate(LibreTubeRoutes.Home)}
        )
    }
}
