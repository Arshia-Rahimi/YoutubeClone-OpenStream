package com.github.openstream.ui.feature.library

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LibraryScreen(
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar {}
    Text("Playlists")
}
