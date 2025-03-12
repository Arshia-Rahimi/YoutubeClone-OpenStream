package com.github.freetube.ui.feature.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
) {
    val viewModel = koinViewModel<SettingsScreenViewModel>()
    val settingsData by viewModel.settingsData.collectAsStateWithLifecycle()
    val appTheme = settingsData.appTheme
    
    Text("SettingsScreen")
}
