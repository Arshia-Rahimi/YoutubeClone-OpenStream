package com.github.freetube.ui.designsystem

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.github.freetube.app.LibreTubeAppState
import com.github.freetube.app.navigation.TopLevelDestinations
import kotlin.reflect.KClass

@Composable
fun LibreTubeScaffold(
    modifier: Modifier = Modifier,
    appState: LibreTubeAppState,
    content: @Composable (Modifier) -> Unit,
) {
    NavigationSuiteScaffold(
        modifier = modifier
            .fillMaxSize(),
        navigationSuiteItems = {
            TopLevelDestinations.entries.forEachIndexed { index, destination ->
                val selected = appState.currentDestination
                    .isInRouteHierarchy(destination.route::class)
                item(
                    selected = selected,
                    onClick = { appState.navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = null,
                        )
                    }
                )
            }
        },
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
            topBar = {
                LibreTubeTopBar()
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = appState.snackbarHostState,
                )
            },
        ) { ip ->
            content(
                Modifier.fillMaxSize()
                    .padding(ip)
                    .consumeWindowInsets(ip)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal
                        ),
                    ),
            )
        }
    }
}

@Composable
private fun LibreTubeTopBar(

) {

}

private fun NavDestination?.isInRouteHierarchy(route: KClass<*>): Boolean =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } == true
