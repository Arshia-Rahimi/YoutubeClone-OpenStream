package com.github.freetube.ui.designsystem.sharedscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PlaylistScreen(
    location: String,
    buttonText: String,
    nav: () -> Unit,
) {
    Column {
        Text("playlist $location")
        Button(onClick = nav) {
            Text(buttonText)
        }
    }
}
