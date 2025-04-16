package com.github.freetube.ui.feature.search.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.freetube.app.navigation.Tabs
import com.github.freetube.core.common.compose.popToRoot
import com.github.freetube.ui.feature.search.SearchScreen
import com.github.freetube.ui.global.channel.ChannelScreen
import com.github.freetube.ui.global.playlist.PlaylistScreen

@Composable
fun SearchNavHost(
    playVideo: (String) -> Unit,
    topBar: (@Composable () -> Unit) -> Unit = {},
) {
    val navController = rememberNavController()
    LaunchedEffect(1) {
        Tabs.Search.navigateToRoot = {
            navController.popToRoot()
        }
        Tabs.Search.doubleClickNavAction = {
            println("doubleClick")
            // todo
        }
    }
    NavHost(
        navController = navController,
        startDestination = Tabs.Search.Main,
    ) {
        composable<Tabs.Search.Main>(
        ) {
            SearchScreen(
                topBar = topBar,
                toChannelScreen = { navController.navigate(Tabs.Search.Channel(it)) },
                toPlaylistScreen = { navController.navigate(Tabs.Search.Playlist(it)) },
                playVideo = playVideo,
            )
        }
        composable<Tabs.Search.Channel> {
            ChannelScreen(
                url = it.toRoute<Tabs.Search.Channel>().url,
                topBar = topBar,
                playVideo = playVideo,
                navigateBack = { navController.popBackStack() },
                toPlaylistScreen = { navController.navigate(Tabs.Search.Playlist(it)) },
            )
        }
        composable<Tabs.Search.Playlist> {
            PlaylistScreen(
                url = it.toRoute<Tabs.Search.Playlist>().url,
                topBar = topBar,
                playVideo = playVideo,
                toChannelScreen = { navController.navigate(Tabs.Search.Channel(it)) },
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}
