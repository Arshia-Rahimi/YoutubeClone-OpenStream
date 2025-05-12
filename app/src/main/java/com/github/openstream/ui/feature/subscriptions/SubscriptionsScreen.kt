package com.github.openstream.ui.feature.subscriptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.openstream.app.navigation.NavigationViewModel

@Composable
fun SubscriptionsScreen(
    navViewModel: NavigationViewModel,
) {
    navViewModel.setTopBar { }
    Box(Modifier
        .fillMaxSize()
        .background(Color.Red))
}
