package com.github.openstream.ui.feature.downloads

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DownloadsScreen(
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar {}
    Text("downloads")
}
