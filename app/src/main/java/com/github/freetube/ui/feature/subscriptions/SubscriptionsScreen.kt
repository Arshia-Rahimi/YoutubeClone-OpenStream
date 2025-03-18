package com.github.freetube.ui.feature.subscriptions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    toPlaylists: () -> Unit,
) {
    val viewModel = koinViewModel<SubscriptionsScreenViewModel>()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("SubscriptionsScreen")
        Button(onClick = toPlaylists) {
            Text("to playlists")
        }
    }
}
