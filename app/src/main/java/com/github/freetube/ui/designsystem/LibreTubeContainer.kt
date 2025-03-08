package com.github.freetube.ui.designsystem

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun LibreTubeContainer(
    modifier: Modifier = Modifier,
    title: String = "LibreTube",
    inSettingsScreen: Boolean = false,
    inHomeScreen: Boolean = false,
    toSettingsScreen: () -> Unit = {},
    toHomeScreen: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    
    LibreTubeScaffold(
        modifier = modifier,
        topBar = {
            LibreTubeTopBar(
                title = title,
                navigationIcon = {
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "navigation drawer",
                        )
                    }
                }
            )
        },
    ) { ip ->
        LibreTubeDrawer(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip),
            inHomeScreen = inHomeScreen,
            inSettingsScreen = inSettingsScreen,
            drawerState = drawerState,
            toSettingsScreen = {
                scope.launch { drawerState.close() }
                toSettingsScreen()
            },
            toHomeScreen = {
                scope.launch { drawerState.close() }
                toHomeScreen()
            }
        ) {
            content()
        }
    }
}
