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
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import com.github.freetube.ui.feature.downloads.DownloadsTab
import com.github.freetube.ui.feature.library.LibraryTab
import com.github.freetube.ui.feature.search.SearchTab
import com.github.freetube.ui.feature.settings.SettingsTab
import com.github.freetube.ui.feature.subscriptions.SubscriptionsTab

val libreTubeTabs = arrayOf(
    SearchTab,
    LibraryTab,
    SubscriptionsTab,
    DownloadsTab,
    SettingsTab,
)

@Composable
fun LibreTubeScaffold(
    currentTab: Tab,
    navigateToTab: (Tab) -> Unit,
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
        modifier = Modifier
            .fillMaxSize(),
        navigationSuiteItems = {
            libreTubeTabs.forEach { tab ->
                val selected = currentTab == tab
                item(
                    selected = selected,
                    onClick = { navigateToTab(tab) },
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (selected) tab.data.selectedIcon else tab.data.icon
                            ),
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(
                            text = tab.options.title,
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
            modifier = Modifier.fillMaxSize(),
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
            topBar = {
                LibreTubeTopBar()
            },
            snackbarHost = {
//                SnackbarHost(
//                    hostState = snackbarHostState,
//                )
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
