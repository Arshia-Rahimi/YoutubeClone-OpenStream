package com.github.freetube.app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.github.freetube.app.navigation.TopLevelDestinations
import com.github.freetube.ui.feature.library.navigation.navigateToLibrary
import com.github.freetube.ui.feature.search.navigation.navigateToSearch
import com.github.freetube.ui.feature.settings.navigation.navigateToSettings
import com.github.freetube.ui.feature.subscriptions.navigation.navigateToSubscriptions

@Composable
fun rememberLibreTubeAppState(
    navHostController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): LibreTubeAppState = remember {
    LibreTubeAppState(
        navController = navHostController,
        snackbarHostState = snackbarHostState,
    )
}

class LibreTubeAppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryAsState()

            return currentEntry.value?.destination.also {
                if (it != null) {
                    previousDestination.value = it
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: TopLevelDestinations
        @Composable get() {
            return TopLevelDestinations.entries.firstOrNull {
                currentDestination?.hasRoute(route = it.route::class) == true
            } ?: TopLevelDestinations.Subscriptions
        }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestinations) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestinations.Subscriptions -> navController.navigateToSubscriptions(topLevelNavOptions)
            TopLevelDestinations.Library -> navController.navigateToLibrary(topLevelNavOptions)
            TopLevelDestinations.Search -> navController.navigateToSearch(topLevelNavOptions)
            TopLevelDestinations.Settings -> navController.navigateToSettings(topLevelNavOptions)
        }
    }
}
