package com.github.freetube.ui.feature.search.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.freetube.app.navigation.SearchRoute
import com.github.freetube.app.navigation.className
import com.github.freetube.core.common.compose.getCurrentRouteClassName
import com.github.freetube.core.common.compose.popToRoot
import com.github.freetube.ui.feature.search.SearchScreen
import com.github.freetube.ui.global.channel.ChannelScreen
import com.github.freetube.ui.global.playlist.PlaylistScreen

@Composable
fun SearchNavHost(
    topBar: (@Composable () -> Unit) -> Unit,
    setTabNavAction: ((() -> Unit)?) -> Unit = { null },
    playVideo: (String) -> Unit,
) {
    val navController = rememberNavController()
    LaunchedEffect(1) {
        setTabNavAction {
            if (navController.getCurrentRouteClassName() != SearchRoute.Main.className()) {
                navController.popToRoot()
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = SearchRoute.Main,
    ) {
        composable<SearchRoute.Main>(
        ) {
            SearchScreen(
                topBar = topBar,
                toChannelScreen = { navController.navigate(SearchRoute.Channel(it)) },
                toPlaylistScreen = { navController.navigate(SearchRoute.Playlist(it)) },
                playVideo = playVideo,
            )
        }
        composable<SearchRoute.Channel> {
            ChannelScreen(
                url = it.toRoute<SearchRoute.Channel>().url,
                topBar = topBar,
                playVideo = playVideo,
                navigateBack = { navController.popBackStack() },
                toPlaylistScreen = { navController.navigate(SearchRoute.Playlist(it)) },
            )
        }
        composable<SearchRoute.Playlist> {
            PlaylistScreen(
                url = it.toRoute<SearchRoute.Playlist>().url,
                topBar = topBar,
                playVideo = playVideo,
                toChannelScreen = { navController.navigate(SearchRoute.Channel(it)) },
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}
