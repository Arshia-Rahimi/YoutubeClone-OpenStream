package com.github.freetube.ui.global.playlist

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.freetube.app.navigation.LibreTubeScreen
import com.github.freetube.ui.global.channel.ChannelDestination
import com.github.freetube.ui.global.player.PlayerScreenModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class PlaylistDestination(
    private val url: String,
) : LibreTubeScreen() {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<PlaylistScreenModel> { parametersOf(url) }
        val playerScreenModel = koinInject<PlayerScreenModel>()
        PlaylistScreen(
            screenModel = screenModel,
            playVideo = { playerScreenModel.start(it) },
            navigateBack = { navigator.pop() },
            topBar = { playerScreenModel.topBar.value = it },
            toChannelScreen = { navigator.push(ChannelDestination(it)) },
        )
    }
}
