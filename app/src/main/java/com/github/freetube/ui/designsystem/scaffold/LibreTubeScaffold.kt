package com.github.freetube.ui.designsystem.scaffold

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.tab.Tab
import com.github.freetube.ui.feature.downloads.DownloadsTab
import com.github.freetube.ui.feature.library.LibraryTab
import com.github.freetube.ui.feature.search.SearchTab
import com.github.freetube.ui.feature.settings.SettingsTab
import com.github.freetube.ui.feature.subscriptions.SubscriptionsTab
import org.koin.compose.koinInject

val libreTubeTabs = arrayOf(
    SearchTab,
    LibraryTab,
    SubscriptionsTab,
    DownloadsTab,
    SettingsTab,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibreTubeScaffold(
    currentTab: Tab,
    navigateToTab: (Tab) -> Unit,
    content: @Composable (Modifier) -> Unit,
) {
    val screenModel = koinInject<ScaffoldScreenModel>()
    val topBar by screenModel.topBar.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
//                .nestedScroll(screenModel.scrollBehavior.nestedScrollConnection),
        containerColor = Color(0xFF111111),
        topBar = { topBar() },
        snackbarHost = {
//                SnackbarHost(
//                    hostState = snackbarHostState,
//                )
        },
        bottomBar = {
            BottomAppBar(
                contentColor = Color(0xFF1A1A1A),
                containerColor = Color(0xFF1A1A1A),
            ) {
                libreTubeTabs.forEach { tab ->
                    val selected = currentTab == tab
                    // todo migrate to m3
                    BottomNavigationItem(
                        selected = selected,
                        onClick = { navigateToTab(tab) },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    if (selected) tab.data.selectedIcon else tab.data.icon
                                ),
                                contentDescription = null,
                                tint = Color.Unspecified,
                            )
                        },
                        label = {
                            Text(
                                text = tab.options.title,
                                maxLines = 1,
                                fontSize = 8.sp,
                                color = if (selected) Color.White else Color(0xFFBABABA)
                            )
                        },
                    )
                }
            }
        }
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
