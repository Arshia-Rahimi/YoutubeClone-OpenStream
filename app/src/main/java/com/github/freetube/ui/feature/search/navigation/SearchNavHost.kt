package com.github.freetube.ui.feature.search.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.freetube.app.navigation.SearchRoute
import com.github.freetube.ui.feature.search.SearchScreen
import com.github.freetube.ui.global.channel.ChannelScreen
import com.github.freetube.ui.global.playlist.PlaylistScreen

@Composable
fun SearchNavHost(
    topBar: (@Composable () -> Unit) -> Unit,
    playVideo: (String) -> Unit,
) {
    val searchNavController = rememberNavController()
    NavHost(
        navController = searchNavController,
        startDestination = SearchRoute.Main,
    ) {
        composable<SearchRoute.Main> {
            SearchScreen(
                topBar = topBar,
                toChannelScreen = { searchNavController.navigate(SearchRoute.Channel(it)) },
                toPlaylistScreen = { searchNavController.navigate(SearchRoute.Playlist(it)) },
                playVideo = playVideo,
            )
        }
        composable<SearchRoute.Channel> {
            ChannelScreen(
                url = it.toRoute<SearchRoute.Channel>().url,
                topBar = topBar,
                playVideo = playVideo,
                navigateBack = { searchNavController.popBackStack() },
                toPlaylistScreen = { searchNavController.navigate(SearchRoute.Playlist(it)) },
            )
        }
        composable<SearchRoute.Playlist> {
            PlaylistScreen(
                url = it.toRoute<SearchRoute.Playlist>().url,
                topBar = topBar,
                playVideo = playVideo,
                toChannelScreen = { searchNavController.navigate(SearchRoute.Channel(it)) },
                navigateBack = { searchNavController.popBackStack() },
            )
        }
    }
}
