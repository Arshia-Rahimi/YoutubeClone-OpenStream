package com.github.freetube.ui.feature.search.main.components

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.freetube.app.navigation.LibreTubeScreen
import com.github.freetube.ui.feature.search.main.SearchScreen
import com.github.freetube.ui.feature.search.main.SearchScreenModel
import com.github.freetube.ui.sharedscreens.channel.ChannelTabScreen
import com.github.freetube.ui.sharedscreens.playlist.PlaylistTabScreen

class SearchTabScreen : LibreTubeScreen() {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<SearchScreenModel>()
        SearchScreen(
            screenModel = screenModel,
            toChannelScreen = { navigator.push(ChannelTabScreen(it)) },
            toPlaylistScreen = { navigator.push(PlaylistTabScreen(it)) },
        )
    }
}
