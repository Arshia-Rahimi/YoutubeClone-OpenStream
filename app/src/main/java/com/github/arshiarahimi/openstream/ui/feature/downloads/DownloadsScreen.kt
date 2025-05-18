package com.github.arshiarahimi.openstream.ui.feature.downloads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.arshiarahimi.openstream.app.navigation.NavigationViewModel

@Composable
fun DownloadsScreen(
    navViewModel: NavigationViewModel,
) {
    navViewModel.setTopBar { }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Green)
    )
}
