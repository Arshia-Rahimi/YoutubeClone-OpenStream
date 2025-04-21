package com.github.openstream.ui.feature.subscriptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SubscriptionsScreen(
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar {}
    Box(Modifier.fillMaxSize().background(Color.Red))
}
