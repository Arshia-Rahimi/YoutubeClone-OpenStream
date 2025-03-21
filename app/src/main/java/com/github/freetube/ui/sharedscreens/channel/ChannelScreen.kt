package com.github.freetube.ui.sharedscreens.channel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.freetube.core.extractor.channel.ChannelInfo
import com.github.freetube.ui.designsystem.LoadingBox
import com.github.freetube.ui.designsystem.scaffold.topBar
import com.github.freetube.ui.sharedscreens.channel.components.ErrorChannel

@Composable
fun ChannelScreen(
    screenModel: ChannelScreenModel,
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
            (uiState as ChannelScreenModel.UiState.Success).channelInfo
        ) { topBar.value = it }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChannelScreen(
    channelInfo: ChannelInfo,
    topBar: (@Composable () -> Unit) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    topBar {
        Box(
            modifier = Modifier.wrapContentSize(),
        ) {
            AsyncImage(
                model = channelInfo.banner,
                contentDescription = "channel banner",
            )
            TopAppBar(
                title = { Text(channelInfo.name) },
                scrollBehavior = scrollBehavior,
                modifier = Modifier
            )
        }
    }
}
