package com.github.freetube.ui.feature.playlists

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsScreen(

) {
    val viewModel = koinViewModel<PlaylistsScreenViewModel>()

    Text("Playlists")
}
