package com.github.freetube.ui.designsystem.sharedscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import io.ktor.websocket.Frame.Text

@Composable
fun ChannelScreen(
    location: String,
    buttonText: String,
    nav: () -> Unit,
) {
    Column {
        Text("channel $location")
        Button(onClick = nav) {
            Text(buttonText)
        }
    }
}
