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
import com.github.freetube.app.LibreTubeAppState
import com.github.freetube.app.navigation.LibreTubeDestinations

@Composable
fun LibreTubeScaffold(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appState: LibreTubeAppState,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            LibreTubeTopBar()
        },
        bottomBar = {
            LibreTubeBottomNavigationBar(
                navController = navController,
                currentDestination = appState.currentTopLevelDestination ?: LibreTubeDestinations.Subscriptions,
            )
        },
    ) { ip ->
        content(ip)
    }
}

@Composable
private fun LibreTubeBottomNavigationBar(
    navController: NavHostController,
    currentDestination: LibreTubeDestinations,
) {
    NavigationBar {
        LibreTubeDestinations.entries.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentDestination == item,
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
}

@Composable
private fun LibreTubeTopBar(
    
) {
    
}
