package com.github.freetube.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.freetube.app.navigation.LibreTubeDestinations
import com.github.freetube.core.model.LibreTubeData

@Composable
fun rememberLibreTubeAppState(
    libreTubeData: LibreTubeData,
    navHostController: NavHostController = rememberNavController(),
): LibreTubeAppState = remember {
    LibreTubeAppState(
        libreTubeData = libreTubeData,
        navController = navHostController,
    )
}

class LibreTubeAppState(
    val libreTubeData: LibreTubeData,
    val navController: NavHostController,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsStateWithLifecycle(initialValue = null)

            return currentEntry.value?.destination.also {
                if (it != null) {
                    previousDestination.value = it
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: LibreTubeDestinations?
        @Composable get() =
            LibreTubeDestinations.entries.firstOrNull {
                currentDestination?.hasRoute(route = it.route::class) == true
            }
}
