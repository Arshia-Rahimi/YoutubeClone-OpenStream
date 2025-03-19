package com.github.freetube.ui.designsystem

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.github.freetube.app.LibreTubeAppState
import com.github.freetube.app.navigation.TopLevelDestinations

@Composable
fun LibreTubeScaffold(
    modifier: Modifier = Modifier,
    appState: LibreTubeAppState,
    currentTLD: TopLevelDestinations,
    content: @Composable (Modifier) -> Unit,
) {
    // todo add lightScheme colors
    val navigationItemColors = NavigationSuiteItemColors(
        NavigationBarItemColors(
            selectedIconColor = Color.Unspecified,
            unselectedIconColor = Color.Unspecified,
            selectedTextColor = Color.White,
            unselectedTextColor = Color.White.copy(0.7f),
            selectedIndicatorColor = Color(0xFF3A2E31),
            disabledTextColor = Color.Unspecified,
            disabledIconColor = Color.Unspecified,
        ),
        NavigationRailItemColors(
            selectedIconColor = Color.Unspecified,
            unselectedIconColor = Color.Unspecified,
            selectedTextColor = Color.White,
            unselectedTextColor = Color.White.copy(0.7f),
            selectedIndicatorColor = Color(0xFF3A2E31),
            disabledTextColor = Color.Unspecified,
            disabledIconColor = Color.Unspecified,
        ),
        NavigationDrawerItemDefaults.colors(
            selectedIconColor = Color.Unspecified,
            unselectedIconColor = Color.Unspecified,
            selectedTextColor = Color.White,
            unselectedTextColor = Color.White.copy(0.7f),
        ),
    )

    NavigationSuiteScaffold(
        modifier = modifier
            .fillMaxSize(),
        navigationSuiteItems = {
            TopLevelDestinations.entries.forEach { destination ->
//                val selected = currentDestination.isInRouteHierarchy(destination.route::class)
                val selected = currentTLD == destination
                item(
                    selected = selected,
                    onClick = { appState.navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (selected) destination.selectedIcon else destination.icon
                            ),
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.label),
                            maxLines = 1,
                            fontSize = 8.sp,
                        )
                    },
                    colors = navigationItemColors,
                )
            }
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Color(0xFF1A1A1A),
            navigationDrawerContainerColor = Color(0xFF1A1A1A),
            navigationRailContainerColor = Color(0xFF1A1A1A),
        ),
        containerColor = Color(0xFF111111),
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
                Modifier
                    .fillMaxSize()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibreTubeTopBar() {
    TopAppBar(
        title = { Text("LibreTube") },
        colors = TopAppBarDefaults.topAppBarColors()
            .copy(
                containerColor = Color(0xFF111111),
                scrolledContainerColor = Color(0xFF111111),
                titleContentColor = Color.White,
            )
    )
}

//private fun NavDestination?.isInRouteHierarchy(route: KClass<*>): Boolean =
//    this?.hierarchy?.any {
//        it.hasRoute(route)
//    } == true
