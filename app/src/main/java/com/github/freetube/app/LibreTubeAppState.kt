package com.github.freetube.app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.github.freetube.app.navigation.TopLevelDestinations
import com.github.freetube.ui.feature.downloads.navigation.navigationToDownloadsScreen
import com.github.freetube.ui.feature.library.navigation.navigateToLibrary
import com.github.freetube.ui.feature.search.navigation.navigateToSearchScreen
import com.github.freetube.ui.feature.settings.navigation.navigateToSettings
import com.github.freetube.ui.feature.subscriptions.navigation.navigateToSubscriptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun rememberLibreTubeAppState(
    navHostController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
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
    private val _currentDestination: MutableStateFlow<NavDestination?> = MutableStateFlow(null)
    val currentDestination = _currentDestination.asStateFlow()

    init {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            _currentDestination.value = destination
        }
    }

    fun navigateToTopLevelDestination(target: TopLevelDestinations) {
        println(currentDestination.value.toString())
        val currentRouteSpecs = currentDestination.value.toString().split(".").takeLast(2)

        // if app is in a "Main" route 
        val isInTopLevelDestination = currentRouteSpecs.last() == "Main"
        // current TLD before navigating
        val currentTLD = currentRouteSpecs.first()

        val topLevelNavOptions = if (currentTLD == target.toString()) {
            // exit function if it's in the target
            if (isInTopLevelDestination) return
            else {
                // pop to current TLD
                navOptions {
                    popUpTo(target.route) {
                        inclusive = true
                        saveState = false
                    }
                    launchSingleTop = true
                    restoreState = false
                }
            }
        } else {
            // moves to another TLD
            navOptions {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = false
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        when (target) {
            TopLevelDestinations.Subscriptions -> navController.navigateToSubscriptions(topLevelNavOptions)
            TopLevelDestinations.Library -> navController.navigateToLibrary(topLevelNavOptions)
            TopLevelDestinations.Search -> navController.navigateToSearchScreen(topLevelNavOptions)
            TopLevelDestinations.Settings -> navController.navigateToSettings(topLevelNavOptions)
            TopLevelDestinations.Downloads -> navController.navigationToDownloadsScreen(topLevelNavOptions)
        }
    }

}
