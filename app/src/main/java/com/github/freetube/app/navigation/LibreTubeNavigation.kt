package com.github.freetube.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.github.freetube.app.LibreTubeAppState
import com.github.freetube.ui.feature.main.navigation.mainScreenNavigation
import com.github.freetube.ui.feature.settings.navigation.settingsScreenNavigation

@Composable
fun LibreTubeNavigation(
    libreTubeAppState: LibreTubeAppState,
) {
    val navController = libreTubeAppState.navController

    NavHost(
        navController = navController,
        startDestination = LibreTubeRoutes.MainRoute
    ) {
        mainScreenNavigation(
            toSettingsScreen = { navController.navigate(LibreTubeRoutes.SettingsRoute) }
        )
        settingsScreenNavigation()
    }
}
