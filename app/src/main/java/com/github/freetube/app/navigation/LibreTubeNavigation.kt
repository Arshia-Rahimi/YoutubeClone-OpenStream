package com.github.freetube.app.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.github.freetube.app.rememberLibreTubeAppState
import com.github.freetube.core.model.LibreTubeData
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import com.github.freetube.ui.feature.playlists.navigation.playlistsScreenNavigation
import com.github.freetube.ui.feature.search.navigation.searchScreenNavigation
import com.github.freetube.ui.feature.settings.navigation.settingsScreenNavigation
import com.github.freetube.ui.feature.subscriptions.navigation.subscriptionsScreenNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibreTubeNavigation(
    libreTubeData: LibreTubeData,
) {
    val appState = rememberLibreTubeAppState(libreTubeData)
    val navController = appState.navController
    val sheetState = rememberModalBottomSheetState()
    
    var showBottomSheet by remember { mutableStateOf(false) }

    LibreTubeScaffold(
        navController = navController,
        appState = appState,
    ) { ip ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(ip),
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = LibreTubeRoutes.Subscriptions
            ) {
                subscriptionsScreenNavigation()
                settingsScreenNavigation()
                playlistsScreenNavigation()
                searchScreenNavigation()
            }
            Row (
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 6.dp, horizontal = 12.dp)
                    .fillMaxWidth()
                    .clickable {
                        showBottomSheet = true
                    },
            ){
                // todo miniPLayer
                Text("miniPlayer")
            }
        }
        if(showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize()
                    .padding(ip),
                onDismissRequest = {},
                sheetState = sheetState,
                dragHandle = {
                    // todo video player
                }
            ) {
                Text("player sheet contents")
            }
        }
    }
}
