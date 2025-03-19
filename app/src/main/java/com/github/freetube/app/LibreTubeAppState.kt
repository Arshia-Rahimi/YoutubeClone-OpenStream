package com.github.freetube.app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.freetube.app.rootcomponent.TopLevelDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun rememberLibreTubeAppState(
    baseNavController: NavHostController = rememberNavController(),
//    settingsNavController: NavHostController = rememberNavController(),
//    downloadsNavController: NavHostController = rememberNavController(),
//    libraryNavController: NavHostController = rememberNavController(),
//    searchNavController: NavHostController = rememberNavController(),
//    subscriptionsNavController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
): LibreTubeAppState = remember {
    LibreTubeAppState(
        baseNavController = baseNavController,
//        settingsNavController = settingsNavController,
//        downloadsNavController = downloadsNavController,
//        subscriptionsNavController = subscriptionsNavController,
//        searchNavController = searchNavController,
//        libraryNavController = libraryNavController,
        snackbarHostState = snackbarHostState,
    )
}

class LibreTubeAppState(
    val baseNavController: NavHostController,
//    val settingsNavController: NavHostController,
//    val subscriptionsNavController: NavHostController,
//    val searchNavController: NavHostController,
//    val libraryNavController: NavHostController,
//    val downloadsNavController: NavHostController,
    val snackbarHostState: SnackbarHostState,
) {
    private val _currentTLD = MutableStateFlow(TopLevelDestinations.Subscriptions)
    val currentTLD = _currentTLD.asStateFlow()

    fun navigateToTopLevelDestination(tld: TopLevelDestinations) {
        if (_currentTLD.value != tld) {
            _currentTLD.value = tld
            baseNavController.navigate(tld.route)
        } else {
            // todo
//            val controller = when(tld) {
//                TopLevelDestinations.Search -> searchNavController
//                TopLevelDestinations.Settings -> settingsNavController
//                TopLevelDestinations.Downloads -> downloadsNavController
//                TopLevelDestinations.Subscriptions -> subscriptionsNavController
//                TopLevelDestinations.Library -> libraryNavController
//            }
//            controller.navigate(
//                navOptions { 
//                    popUpTo(tld.mainRoute)
//                }
//            )
        }
    }

//    private val _currentDestination: MutableStateFlow<NavDestination?> = MutableStateFlow(null)
//    val currentDestination = _currentDestination.asStateFlow()
//
//    init {
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            _currentDestination.value = destination
//        }
//    }
//
//    fun navigateToTopLevelDestination(target: TopLevelDestinations) {
//        val currentRouteSpecs = currentDestination.value.toString().split(".").takeLast(2)
//
//        // if app is in a "Main" route 
//        val isInTopLevelDestination = currentRouteSpecs.last() == "Main"
//        // current TLD before navigating
//        val currentTLD = currentRouteSpecs.first()
//
//        if (currentTLD == target.toString()) {
//            // exit function if it's in the target
//            if (isInTopLevelDestination) return
//            else {
//                // pop to current TLD
//                navController.navigate(
//                    route = target.mainRoute,
//                    navOptions = navOptions {
////                        popUpTo(target.mainRoute) {
////                            saveState = true
////                        }
////                        restoreState = true
////                        launchSingleTop = true
//                    }
//                )
//            }
//        } else {
//            // nav to another TLD
//            navController.navigate(
//                route = target.mainRoute,
//                navOptions = navOptions {
////                    popUpTo(navController.graph.startDestinationId) {
////                        saveState = true
////                    }
////                    restoreState = true
////                    launchSingleTop = true
//                }
//            )
//        }
//    }
}
