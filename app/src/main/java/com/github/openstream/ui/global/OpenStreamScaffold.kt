package com.github.openstream.ui.global

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.openstream.app.navigation.NavigationViewModel.Companion.tabsList
import com.github.openstream.app.navigation.routes.Tabs
import com.github.openstream.core.common.compose.ObserveForEvents
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.ui.global.player.PlayerSheet
import com.github.openstream.ui.global.popups.PopupController
import com.github.openstream.ui.global.popups.addtoplaylistmodal.SaveVideoToPlaylistsModal
import com.github.openstream.ui.global.popups.createplaylistdialog.CreatePlaylistDialog
import kotlinx.coroutines.launch

@Composable
fun OpenStreamScaffold(
    currentTab: Tabs,
    navAction: (Tabs, Boolean) -> Unit,
    toChannelScreen: (String) -> Unit,
    topBar: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var navBarOffset by remember { mutableFloatStateOf(0f) }
    val showSaveVideoToPlaylistsModal by PopupController.showSaveVideoToPlaylistsModal.collectAsStateWithLifecycle()
    val showCreatePlaylistDialog by PopupController.showCreatePlaylistDialog.collectAsStateWithLifecycle()

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

    showSaveVideoToPlaylistsModal?.let {
        SaveVideoToPlaylistsModal(
            video = it,
        )
    }

    if (showCreatePlaylistDialog) {
        CreatePlaylistDialog()
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
                navAction = navAction,
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
    currentTab: Tabs,
    setNavBarOffset: (Float) -> Unit,
    navAction: (Tabs, Boolean) -> Unit,
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
            val isSelected = tab == currentTab
            var lastClickTime by remember { mutableLongStateOf(0L) }
            val doubleTapInterval = 300
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    val currentTime = System.currentTimeMillis()
                    val isDoubleClick = (currentTime - lastClickTime) < doubleTapInterval
                    lastClickTime = if (isDoubleClick) 0L else currentTime
                    navAction(tab, isDoubleClick)
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (isSelected) tab.selectedIcon else tab.icon
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
                        color = if (isSelected) Color.White else Color(0xFFBABABA)
                    )
                },
            )
        }
    }
}
