package com.github.freetube.ui.global.channel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.core.extractor.channel.ChannelInfo
import com.github.freetube.ui.designsystem.LoadingBox
import com.github.freetube.ui.global.channel.components.ChannelTopBar
import com.github.freetube.ui.global.channel.components.ErrorChannel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(
    screenModel: ChannelScreenModel,
    topBar: (@Composable () -> Unit) -> Unit,
    navigateBack: () -> Unit,
) {
    val uiState by screenModel.state.collectAsStateWithLifecycle()

    when (uiState) {
        is ChannelScreenModel.UiState.Loading -> LoadingBox()
        is ChannelScreenModel.UiState.Error -> ErrorChannel(
            (uiState as ChannelScreenModel.UiState.Error).message,
            navigateBack
        )
        is ChannelScreenModel.UiState.Success -> ChannelScreen(
            (uiState as ChannelScreenModel.UiState.Success).channelInfo,
            topBar = topBar,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChannelScreen(
    channelInfo: ChannelInfo,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    topBar { ChannelTopBar(channelInfo) }
}
