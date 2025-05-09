package com.github.openstream.ui.feature.library.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.openstream.app.navigation.routes.OpenStreamNavTypes
import com.github.openstream.app.navigation.routes.Tabs
import com.github.openstream.core.common.compose.getCurrentRouteClassName
import com.github.openstream.core.common.compose.popToRoot
import com.github.openstream.core.model.extractordata.PlaylistItem
import com.github.openstream.ui.feature.library.LibraryScreen
import com.github.openstream.ui.global.screens.channel.ChannelScreen
import com.github.openstream.ui.global.screens.playlist.PlaylistScreen
import kotlin.reflect.typeOf

@Composable
fun LibraryNavHost(
    playVideo: (String) -> Unit,
    topBar: (@Composable () -> Unit) -> Unit = {},
) {
    val navController = rememberNavController()
        .apply {
            addOnDestinationChangedListener { controller, _, _ ->
                Tabs.Library.isInTabRoot.value =
                    controller.getCurrentRouteClassName()?.let { it == "Root" } != false
            }
        }

    LaunchedEffect(navController) {
        Tabs.Search.navigateToCurrentTabRoot = {
            if (navController.getCurrentRouteClassName() != Tabs.Library.Root.toString()) {
                navController.popToRoot()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Tabs.Library.Root,
    ) {
        composable<Tabs.Library.Root> {
            LibraryScreen(
                topBar = topBar,
                toChannelScreen = { navController.navigate(Tabs.Library.Channel(it)) },
                toPlaylistScreen = { navController.navigate(Tabs.Library.Playlist(it)) },
                playVideo = playVideo,
            )
        }
        composable<Tabs.Library.Channel> {
            ChannelScreen(
                url = it.toRoute<Tabs.Search.Channel>().url,
                topBar = topBar,
                playVideo = playVideo,
                navigateBack = { navController.popBackStack() },
                toPlaylistScreen = { navController.navigate(Tabs.Search.Playlist(it)) },
            )
        }
        composable<Tabs.Library.Playlist>(
            typeMap = mapOf(
                typeOf<PlaylistItem>() to OpenStreamNavTypes.playlistType,
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
