package com.github.openstream.ui.feature.search.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.openstream.core.common.compose.getCurrentRouteClassName
import com.github.openstream.core.common.compose.popToRoot
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.feature.search.SearchScreen
import com.github.openstream.ui.global.reusablescreens.channel.ChannelScreen
import com.github.openstream.ui.global.reusablescreens.playlist.PlaylistScreen
import com.github.openstream.ui.navigation.OpenStreamNavTypes
import com.github.openstream.ui.navigation.Tabs
import kotlin.reflect.typeOf

@Composable
fun SearchNavHost(
    playVideo: (String) -> Unit,
    topBar: (@Composable () -> Unit) -> Unit = {},
) {
    val navController = rememberNavController()
        .apply {
            addOnDestinationChangedListener { controller, _, _ ->
                Tabs.Search.isInTabRoot.value =
                    controller.getCurrentRouteClassName()?.let { it == "Root" } != false
            }
        }

    LaunchedEffect(navController) {
        Tabs.Search.navigateToCurrentTabRoot = {
            if (navController.getCurrentRouteClassName() != Tabs.Search.Root.toString()) {
                navController.popToRoot()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Tabs.Search.Root,
    ) {
        composable<Tabs.Search.Root> {
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
        composable<Tabs.Search.Playlist>(
            typeMap = mapOf(
                typeOf<DataItem.Playlist>() to OpenStreamNavTypes.playlistType,
            ),
        ) {
            PlaylistScreen(
                playlist = it.toRoute<Tabs.Search.Playlist>().playlist,
                topBar = topBar,
                playVideo = playVideo,
                toChannelScreen = { navController.navigate(Tabs.Search.Channel(it)) },
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}
