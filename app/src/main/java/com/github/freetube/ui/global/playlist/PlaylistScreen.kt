package com.github.freetube.ui.global.playlist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PlaylistScreen(
    screenModel: PlaylistScreenModel,
    topBar: (@Composable () -> Unit) -> Unit,
    playVideo: (String) -> Unit,
    navigateBack: () -> Unit,
    toChannelScreen: (String) -> Unit,
) {
    Text("playlist")
}
