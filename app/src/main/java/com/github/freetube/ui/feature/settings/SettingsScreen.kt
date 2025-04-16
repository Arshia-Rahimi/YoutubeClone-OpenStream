package com.github.freetube.ui.feature.settings

import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsScreen(
    topBar: (@Composable () -> Unit) -> Unit,
) {
    Text("settings", modifier = Modifier.background(Color.Green))
}
