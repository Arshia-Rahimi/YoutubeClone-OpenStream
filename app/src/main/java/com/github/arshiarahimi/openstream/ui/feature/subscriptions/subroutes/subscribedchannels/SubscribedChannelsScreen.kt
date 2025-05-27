package com.github.arshiarahimi.openstream.ui.feature.subscriptions.subroutes.subscribedchannels

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.DataItemList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribedChannelsScreen(
    toChannelScreen: (String) -> Unit,
) {
    val viewModel = koinViewModel<SubscribedChannelsViewModel>()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Subscribed Channels") }
            )
        }
    ) { ip ->
        DataItemList(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip),
            items = viewModel.subscriptions,
            toChannelScreen = toChannelScreen,
        )
    }
}
