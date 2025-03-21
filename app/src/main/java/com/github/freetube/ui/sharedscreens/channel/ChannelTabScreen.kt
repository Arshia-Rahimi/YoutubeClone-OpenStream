package com.github.freetube.ui.sharedscreens.channel

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import com.github.freetube.app.navigation.LibreTubeScreen
import org.koin.core.parameter.parametersOf

data class ChannelTabScreen(
    private val url: String,
) : LibreTubeScreen() {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ChannelScreenModel> { parametersOf(url) }
        ChannelScreen(
            screenModel = screenModel,
        )
    }
}
