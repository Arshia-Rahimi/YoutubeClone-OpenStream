package com.github.freetube.ui.global.player

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.app.navigation.Tabs
import com.github.freetube.app.navigation.TopLevelDestination
import com.github.freetube.ui.common.ObserveForEvents
import com.github.freetube.ui.common.snackbar.SnackBarController
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun LibreTubeScaffold(
    currentTab: String,
    navigateToTab: (TopLevelDestination) -> Unit,
    content: @Composable () -> Unit,
) {
    val viewModel = koinInject<PlayerViewModel>()
    val topBar by viewModel.topBar.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                event.action?.action()
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { topBar() },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        bottomBar = {
            BottomBar(
                currentTab = currentTab,
                navigateToTab = navigateToTab,
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
        viewModel = viewModel,
        toChannelScreen = {},
    )
}

@Composable
private fun BottomBar(
    currentTab: String,
    navigateToTab: (TopLevelDestination) -> Unit,
) {
    NavigationBar(
        contentColor = Color(0xFF1A1A1A),
        containerColor = Color(0xFF1A1A1A),
    ) {
        Tabs.entries.forEach { tab ->
            val selected = currentTab == tab.route.toString()
            NavigationBarItem(
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
