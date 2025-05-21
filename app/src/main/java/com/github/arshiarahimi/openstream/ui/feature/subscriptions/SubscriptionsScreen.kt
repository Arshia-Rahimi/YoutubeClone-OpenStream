package com.github.arshiarahimi.openstream.ui.feature.subscriptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.arshiarahimi.openstream.R
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.ui.designsystem.components.dataitem.DataItemList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    topBar: (@Composable () -> Unit) -> Unit,
    toPlaylistScreen: (PlaylistItem) -> Unit,
    toChannelScreen: (String) -> Unit,
    toSubscribedChannelsScreen: () -> Unit,
    playVideo: (String) -> Unit,
) {
    val viewModel = koinViewModel<SubscriptionsViewModel>()

    topBar {
        TopAppBar(
            title = {
                Text(stringResource(R.string.subs))
            },
            actions = {
                IconButton(
                    onClick = toSubscribedChannelsScreen,
                ) {
                    Icon(
                        contentDescription = "subscribed channels",
                        imageVector = Icons.AutoMirrored.Filled.List,
                    )
                }
            }
        )
    }

    DataItemList(
        items = viewModel.videos,
        toChannelScreen = toChannelScreen,
        toPlaylistScreen = toPlaylistScreen,
        playVideo = playVideo,
    )
}
