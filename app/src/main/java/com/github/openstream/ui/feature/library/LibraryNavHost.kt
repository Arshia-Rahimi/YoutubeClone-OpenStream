package com.github.openstream.ui.feature.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.openstream.app.navigation.NavigationViewModel
import com.github.openstream.app.navigation.isInTabRoot
import com.github.openstream.app.navigation.routes.OpenStreamNavTypes
import com.github.openstream.app.navigation.routes.Tabs
import com.github.openstream.core.common.compose.popToRoot
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.ui.feature.library.root.LibraryScreen
import com.github.openstream.ui.global.screens.channel.ChannelScreen
import com.github.openstream.ui.global.screens.playlist.PlaylistScreen
import kotlin.reflect.typeOf

@Composable
fun LibraryNavHost(
    navViewModel: NavigationViewModel,
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        navViewModel.tabClickAction
            .collect {
                if (it == Tabs.Library) {
                    if (!navController.isInTabRoot()) {
                        navController.popToRoot()
                    }
                }
            }
    }
    LaunchedEffect(Unit) {
        navViewModel.navigateToChannelScreenFromSheetEvent
            .collect {
                if (it.first == Tabs.Library)
                    navController.navigate(Tabs.Library.Channel(it.second))
            }
    }

    NavHost(
        navController = navController,
        startDestination = Tabs.Library.Root,
    ) {
        composable<Tabs.Library.Root> {
            LibraryScreen(
                toChannelScreen = { url -> navController.navigate(Tabs.Library.Channel(url)) },
                toPlaylistScreen = { url -> navController.navigate(Tabs.Library.Playlist(url)) },
            )
        }
        composable<Tabs.Library.Channel> {
            ChannelScreen(
                url = it.toRoute<Tabs.Library.Channel>().url,
                navigateBack = { navController.popBackStack() },
                toPlaylistScreen = { url -> navController.navigate(Tabs.Library.Playlist(url)) },
            )
        }
        composable<Tabs.Library.Playlist>(
            typeMap = mapOf(
                typeOf<PlaylistItem>() to OpenStreamNavTypes.playlistType,
            ),
        ) {
            PlaylistScreen(
                playlist = it.toRoute<Tabs.Library.Playlist>().playlist,
                toChannelScreen = { url -> navController.navigate(Tabs.Library.Channel(url)) },
                navigateBack = { navController.navigateUp() },
            )
        }
    }
}
