package com.github.freetube.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.freetube.ui.designsystem.scaffold.LibreTubeScaffold
import com.github.freetube.ui.feature.subscriptions.SubscriptionsTab
import com.github.freetube.ui.global.player.MiniPlayer
import com.github.freetube.ui.global.player.PlayerScreenModel
import com.github.freetube.ui.global.player.PlayerSheet
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabNavigation() {
    val screenModel = koinInject<PlayerScreenModel>()
    var showBottomSheet by remember { mutableStateOf(false) }
    val showMiniPlayer by screenModel.showMiniPlayer.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    TabNavigator(SubscriptionsTab) { navigator ->
        val tabNavigator = LocalTabNavigator.current
        val currentTab by remember { derivedStateOf { tabNavigator.current } }
        LibreTubeScaffold(
            currentTab = currentTab,
            navigateToTab = { navigator.current = it },
        ) { modifier ->
            Box(
                modifier = modifier,
            ) {
                CurrentTab()
                if (showMiniPlayer) {
                    MiniPlayer(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        screenModel = screenModel,
                        showBottomSheet = { showBottomSheet = true },
                        shouldShowMiniPlayer = !sheetState.isVisible,
                    )
                }
            }
            if (showBottomSheet) {
                PlayerSheet(
                    screenModel = screenModel,
                    dismissSheet = { showBottomSheet = false },
                    toChannelScreen = {
                        // todo
                    },
                    sheetState = sheetState,
                )
            }
        }
    }
}
