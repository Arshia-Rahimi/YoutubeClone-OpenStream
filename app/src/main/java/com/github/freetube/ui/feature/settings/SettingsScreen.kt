package com.github.freetube.ui.feature.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = koinViewModel(),
) {
    LibreTubeScaffold { ip ->
        Text("SettingsScreen")
    }
}
