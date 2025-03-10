package com.github.freetube.ui.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.freetube.ui.designsystem.LibreTubeDrawer
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import com.github.freetube.ui.feature.home.components.HomeScreenTopBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel(),
    toSettingsScreen: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by viewModel.searchText

    LibreTubeScaffold(
        topBar = {
            HomeScreenTopBar(
                showSearchBar = showSearchBar,
                setShowSearchBar = { showSearchBar = it },
                drawerState = drawerState,
                scope = scope,
                searchText = searchText,
                setSearchText = { searchText = it }
            )
        },
    ) { ip ->
        LibreTubeDrawer(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip),
            inHomeScreen = true,
            drawerState = drawerState,
            toSettingsScreen = {
                scope.launch { drawerState.close() }
                toSettingsScreen()
            },
            toHomeScreen = {
                scope.launch { drawerState.close() }
            }
        ) {

        }
    }
}
