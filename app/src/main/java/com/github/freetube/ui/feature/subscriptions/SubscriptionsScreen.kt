package com.github.freetube.ui.feature.subscriptions

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.freetube.ui.global.player.PlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubscriptionsScreen() {
    val playerViewModel = koinViewModel<PlayerViewModel>()
    Button(
        onClick = { playerViewModel.start("") },
    ) {
        Text("dasfkjnaf")
    }
}
