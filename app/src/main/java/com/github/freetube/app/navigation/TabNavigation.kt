package com.github.freetube.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.freetube.ui.feature.subscriptions.SubscriptionsTab
import com.github.freetube.ui.global.player.LibreTubeScaffold

@Composable
fun TabNavigation() {
    TabNavigator(SubscriptionsTab) { navigator ->
        val currentTab by remember { derivedStateOf { navigator.current } }
        LibreTubeScaffold(
            currentTab = currentTab,
            navigateToTab = { navigator.current = it },
        ) { modifier ->
            Box(Modifier) {
                CurrentTab()
            }
        }
    }
}
