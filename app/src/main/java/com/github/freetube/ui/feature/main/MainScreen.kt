package com.github.freetube.ui.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import com.github.freetube.ui.designsystem.LibreTubeTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: MainScreenViewModel = koinViewModel(),
    toSettingsScreen: () -> Unit,
) {
    var drawerState = rememberDrawerState(DrawerValue.Closed)

    LibreTubeScaffold(
        topBar = {
            LibreTubeTopBar(
                title = "LibreTube",
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
        }
    ) { ip ->
        ModalNavigationDrawer(
            modifier = Modifier.fillMaxSize()
                .padding(ip),
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(drawerState) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Settings",
                                )
                            },
                            label = { Text("Settings") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                toSettingsScreen()
                            },
                        )
                    }
                }
            }
        ) {
            
        }
    }
}
