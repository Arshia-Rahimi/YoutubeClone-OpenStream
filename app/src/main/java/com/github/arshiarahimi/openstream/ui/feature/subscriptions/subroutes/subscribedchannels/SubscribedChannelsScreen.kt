package com.github.arshiarahimi.openstream.ui.feature.subscriptions.subroutes.subscribedchannels

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.DataItemList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribedChannelsScreen(
    topBar: (@Composable () -> Unit) -> Unit,
    toChannelScreen: (String) -> Unit,
) {
    val viewModel = koinViewModel<SubscribedChannelsViewModel>()

    topBar {
        TopAppBar(
            title = { Text("Subscribed Channels") }
        )
    }

    DataItemList(
        items = viewModel.subscriptions,
        toChannelScreen = toChannelScreen,
    )
}
