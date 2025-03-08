package com.github.freetube.ui.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LibreTubeDrawer(
    modifier: Modifier = Modifier,
    inHomeScreen: Boolean = false,
    inSettingsScreen: Boolean = false,
    drawerState: DrawerState,
    toHomeScreen: () -> Unit = {},
    toSettingsScreen: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerState) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(
                        space = 12.dp,
                        alignment = Alignment.Top,
                    ),
                ) {
                    item {
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Home,
                                    contentDescription = "Home",
                                )
                            },
                            label = { Text("Home") },
                            selected = inHomeScreen,
                            onClick = toHomeScreen,
                        )
                    }
                    item {
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Settings",
                                )
                            },
                            label = { Text("Settings") },
                            selected = inSettingsScreen,
                            onClick = toSettingsScreen,
                        )
                    }
                }
            }
        }
    ) {
        content()
    }
}
