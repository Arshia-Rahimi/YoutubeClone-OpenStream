package com.github.freetube.app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.github.freetube.app.navigation.TopLevelDestinations
import com.github.freetube.ui.feature.downloads.navigation.navigationToDownloadsScreen
import com.github.freetube.ui.feature.library.navigation.navigateToLibrary
import com.github.freetube.ui.feature.search.navigation.navigateToSearchScreen
import com.github.freetube.ui.feature.settings.navigation.navigateToSettings
import com.github.freetube.ui.feature.subscriptions.navigation.navigateToSubscriptions

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
    private val previousDestination = mutableStateOf<NavDestination?>(null)
    private var currentDestinationFromListener: NavDestination? = null
    private var currentTopLevelDestinationFromListener: TopLevelDestinations =
        TopLevelDestinations.Subscriptions

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

    // todo tessssssssssssst this
    fun navigateToTopLevelDestination(target: TopLevelDestinations) {
        val currentRouteList = currentDestinationFromListener.toString().split(".")
        val isInTopLevelDestination = currentRouteList.last() == "Main"

        // current TLD before navigating
        val currentTLD = currentRouteList.dropLast(1).last()

        val topLevelNavOptions = if (currentTLD == target.toString()) {
            // exit function if it's in the target
            if (isInTopLevelDestination) return
            else {
                println("pop to current TLD")
                // pop to current TLD
                navOptions {
                    popUpTo(target.route)
                    launchSingleTop = true
                    restoreState = false
                }
            }
        } else {
            println("nav to another TLD")
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

    init {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestinationFromListener = destination
            currentTopLevelDestinationFromListener = TopLevelDestinations.entries
                .firstOrNull {
                    currentDestinationFromListener?.hasRoute(it.route::class) == true
                } ?: TopLevelDestinations.Subscriptions
        }
    }
}
