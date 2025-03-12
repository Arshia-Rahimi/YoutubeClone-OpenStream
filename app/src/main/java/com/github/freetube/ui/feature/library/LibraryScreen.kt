package com.github.freetube.ui.feature.library

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(

) {
    val viewModel = koinViewModel<LibraryScreenViewModel>()

    Text("Playlists")
}
