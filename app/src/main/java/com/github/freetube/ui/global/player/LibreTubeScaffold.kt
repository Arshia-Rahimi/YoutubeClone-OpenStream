package com.github.freetube.ui.global.player

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.github.freetube.app.navigation.Tabs
import com.github.freetube.app.navigation.tabsList
import com.github.freetube.core.common.compose.ObserveForEvents
import com.github.freetube.core.common.compose.SnackBarController
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LibreTubeScaffold(
    currentTab: String,
    navigateToTab: (Tabs) -> Unit,
    toChannelScreen: (String) -> Unit,
    topBar: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var navBarOffset by remember { mutableFloatStateOf(0f) }

    ObserveForEvents(SnackBarController.events) { event ->
        scope.launch {
            if (event.isImmediate) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = event.duration,
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { topBar?.invoke() },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        bottomBar = {
            BottomBar(
                currentTab = currentTab,
                navigateToTab = navigateToTab,
                setNavBarOffset = { navBarOffset = it },
            )
        }
    ) { ip ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip)
                .consumeWindowInsets(ip)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    ),
                ),
        ) {
            content()
        }
    }
    PlayerSheet(
        navBarOffset = navBarOffset,
        toChannelScreen = toChannelScreen,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomBar(
    currentTab: String,
    setNavBarOffset: (Float) -> Unit,
    navigateToTab: (Tabs) -> Unit,
) {
    NavigationBar(
        contentColor = Color(0xFF1A1A1A),
        containerColor = Color(0xFF1A1A1A),
        modifier = Modifier
            .background(Color.Cyan)
            .onGloballyPositioned {
                setNavBarOffset(it.boundsInRoot().top)
            }
    ) {
        tabsList.forEach { tab ->
            val selected = currentTab == tab.toString()
            var lastClickTime by remember { mutableLongStateOf(0L) }
            val doubleTapInterval = 300
            NavigationBarItem(
                selected = selected,
                onClick = {
                    val currentTime = System.currentTimeMillis()
                    val isDoubleClick = (currentTime - lastClickTime) < doubleTapInterval
                    navigateToTab(tab)
                    lastClickTime = if (isDoubleClick) 0L else currentTime
                    if (isDoubleClick) tab.doubleClickNavAction?.invoke()
                },
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
