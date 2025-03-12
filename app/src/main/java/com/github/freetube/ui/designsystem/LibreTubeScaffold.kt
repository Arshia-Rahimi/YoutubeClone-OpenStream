package com.github.freetube.ui.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.freetube.app.navigation.LibreTubeDestinations

@Composable
fun LibreTubeScaffold(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        bottomBar = {
            NavigationBar {
                LibreTubeDestinations.entries.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == item.ordinal,
                        onClick = { navController.navigate(item.route) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                            )
                        },
                    )
                }
            }
        },
    ) { ip ->
        content(ip)
    }
}
