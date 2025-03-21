package com.github.freetube.ui.sharedscreens.playlist

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import com.github.freetube.app.navigation.LibreTubeScreen
import org.koin.core.parameter.parametersOf

class PlaylistTabScreen(
    private val url: String,
) : LibreTubeScreen() {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<PlaylistScreenModel> { parametersOf(url) }
        PlaylistScreen(screenModel)
    }
}
