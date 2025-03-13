package com.github.freetube.ui.feature.downloads

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun DownloadsScreen() {
    val viewModel = koinViewModel<DownloadsScreenViewModel>()

    Text("downloads")
}
