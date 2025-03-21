package com.github.freetube.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import com.github.freetube.ui.feature.subscriptions.SubscriptionsTab
import com.github.freetube.ui.global.player.MiniPlayer
import com.github.freetube.ui.global.player.PlayerSheet

@Composable
fun TabNavigation() {
    var showBottomSheet by remember { mutableStateOf(false) }

    TabNavigator(SubscriptionsTab) { navigator ->
        val tabNavigator = LocalTabNavigator.current
        val currentTab by remember { derivedStateOf { tabNavigator.current } }
        LibreTubeScaffold(
            currentTab = currentTab,
            navigateToTab = { navigator.current = it }
        ) { modifier ->
            Box(
                modifier = modifier,
            ) {
                CurrentTab()
                MiniPlayer(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    showBottomSheet = { showBottomSheet = true }
                )
            }
            if (showBottomSheet) PlayerSheet()
        }
    }
}
