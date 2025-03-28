package com.github.freetube.ui.feature.search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.freetube.app.navigation.LibreTubeScreen
import com.github.freetube.ui.designsystem.scaffold.ScaffoldScreenModel
import com.github.freetube.ui.global.channel.ChannelDestination
import com.github.freetube.ui.global.player.PlayerScreenModel
import com.github.freetube.ui.global.playlist.PlaylistDestination
import org.koin.compose.koinInject

class SearchDestination : LibreTubeScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<SearchScreenModel>()
        val scaffoldScreenModel = koinInject<ScaffoldScreenModel>()
        val playerScreenModel = koinInject<PlayerScreenModel>()
        SearchScreen(
            screenModel = screenModel,
            topBar = { scaffoldScreenModel.topBar.value = it },
            toChannelScreen = { navigator.push(ChannelDestination(it)) },
            toPlaylistScreen = { navigator.push(PlaylistDestination(it)) },
            playVideo = { playerScreenModel.start(it) },
        )
    }
}
