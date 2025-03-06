package com.github.freetube.ui.feature.main

import androidx.compose.runtime.Composable
import com.github.freetube.ui.designsystem.LibreTubeScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = koinViewModel(),
    toSettingsScreen: () -> Unit,
) {
    LibreTubeScaffold { ip ->
        
    } 
}
