package com.github.freetube.ui.feature.downloads

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DownloadsScreen(
    setTabNavAction: ((() -> Unit)?) -> Unit = { null },
) {
    Text("downloads")
}
