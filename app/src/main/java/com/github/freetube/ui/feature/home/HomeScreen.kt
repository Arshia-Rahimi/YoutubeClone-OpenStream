package com.github.freetube.ui.feature.home

import androidx.compose.runtime.Composable
import com.github.freetube.ui.designsystem.LibreTubeContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel(),
    toSettingsScreen: () -> Unit,
) {
    LibreTubeContainer(
        toSettingsScreen = toSettingsScreen,
        inHomeScreen = true,
    ) {
        
    }
}
