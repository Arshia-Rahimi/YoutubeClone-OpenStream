package com.github.freetube.ui.feature.search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.freetube.app.navigation.LibreTubeScreen
import com.github.freetube.ui.designsystem.scaffold.ScaffoldScreenModel
import com.github.freetube.ui.global.channel.ChannelTabScreen
import com.github.freetube.ui.global.playlist.PlaylistTabScreen
import org.koin.compose.koinInject

class SearchTabScreen : LibreTubeScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<SearchScreenModel>()
        val scaffoldScreenModel = koinInject<ScaffoldScreenModel>()
        SearchScreen(
            screenModel = screenModel,
            topBar = { scaffoldScreenModel.topBar.value = it },
            toChannelScreen = { navigator.push(ChannelTabScreen(it)) },
            toPlaylistScreen = { navigator.push(PlaylistTabScreen(it)) },
        )
    }
}
