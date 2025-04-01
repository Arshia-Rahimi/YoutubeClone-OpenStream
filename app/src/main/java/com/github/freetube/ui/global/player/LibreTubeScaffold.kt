package com.github.freetube.ui.global.player

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.github.freetube.app.navigation.Tabs
import com.github.freetube.app.navigation.TopLevelDestination
import org.koin.compose.koinInject
import kotlin.reflect.KClass

const val LOADING_SHARED_KEY = ""

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun LibreTubeScaffold(
    sheetScaffoldState: BottomSheetScaffoldState,
    currentEntry: NavDestination?,
    navigateToTab: (TopLevelDestination) -> Unit,
    content: @Composable (Modifier) -> Unit,
) {
    val viewModel = koinInject<PlayerViewModel>()
    val topBar by viewModel.topBar.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                currentEntry = currentEntry,
                navigateToTab = navigateToTab,
            )
        }
    ) {
        SharedTransitionLayout {
            AnimatedContent(sheetScaffoldState.bottomSheetState.currentValue) { state ->
                BottomSheetScaffold(
                    sheetPeekHeight = 150.dp,
                    modifier = Modifier
                        .fillMaxSize(),
                    scaffoldState = sheetScaffoldState,
//                .nestedScroll(screenModel.scrollBehavior.nestedScrollConnection),
                    topBar = { topBar() },
                    snackbarHost = {
//                SnackbarHost(
//                    hostState = snackbarHostState,
//                )
                    },
                    sheetDragHandle = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.sharedElement(
                                    state = rememberSharedContentState(LOADING_SHARED_KEY),
                                    animatedVisibilityScope = this@AnimatedContent,
                                )
                            )
                        }
                    },
                    sheetContent = {
                        PlayerSheet(
                            viewModel = viewModel,
                            toChannelScreen = {
                                // todo
                            },
                            sheetValue = state,
                            animatedVisibilityScope = this@AnimatedContent,
                            sheetState = sheetScaffoldState.bottomSheetState,
                            columnScope = this,
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
    }
}

@Composable
private fun BottomBar(
    currentEntry: NavDestination?,
    navigateToTab: (TopLevelDestination) -> Unit,
) {
    BottomAppBar(
        contentColor = Color(0xFF1A1A1A),
        containerColor = Color(0xFF1A1A1A),
    ) {
        Tabs.entries.forEach { tab ->
            val selected = currentEntry.isRouteInHierarchy(tab.route::class)
            // todo migrate to m3
            BottomNavigationItem(
                modifier = Modifier.navigationBarsPadding(),
                selected = selected,
                onClick = { navigateToTab(tab.route) },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (selected) tab.selectedIcon else tab.icon
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                },
                label = {
                    Text(
                        text = stringResource(tab.title),
                        maxLines = 1,
                        fontSize = 8.sp,
                        color = if (selected) Color.White else Color(0xFFBABABA)
                    )
                },
            )
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } == true
