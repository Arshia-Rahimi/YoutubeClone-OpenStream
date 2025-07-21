package com.github.openstream.ui.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.openstream.core.common.compose.popToRoot
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.ui.feature.search.root.SearchScreen
import com.github.openstream.ui.global.reusable.screens.channel.ChannelScreen
import com.github.openstream.ui.global.reusable.screens.playlist.PlaylistScreen
import com.github.openstream.ui.navigation.NavigationViewModel
import com.github.openstream.ui.navigation.isInTabRoot
import com.github.openstream.ui.navigation.routes.OpenStreamNavTypes
import com.github.openstream.ui.navigation.routes.Tabs
import kotlin.reflect.typeOf

@Composable
fun SearchNavHost(
    navViewModel: NavigationViewModel,
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        navViewModel.tabClickAction
            .collect {
                if (it == Tabs.Search) {
                    if (!navController.isInTabRoot()) {
                        navController.popToRoot()
                    }
                }
            }
    }
    LaunchedEffect(Unit) {
        navViewModel.navigateToChannelScreenFromSheetEvent
            .collect {
                if (it.first == Tabs.Search)
                    navController.navigate(Tabs.Search.Channel(it.second))
            }
    }
    LaunchedEffect(Unit) {
        navViewModel.navigateToPlaylistScreenFromSheetEvent
            .collect {
                if (it.first == Tabs.Search)
                    navController.navigate(
                        Tabs.Search.Playlist(
                            PlaylistItem.OnlinePlaylistItem(
                                "",
                                null,
                                0L,
                                "",
                                "",
                                false,
                                it.second
                            )
                        )
                    )
            }
    }

    NavHost(
        navController = navController,
        startDestination = Tabs.Search.Root,
    ) {
        composable<Tabs.Search.Root> {
            SearchScreen(
                toChannelScreen = { url -> navController.navigate(Tabs.Search.Channel(url)) },
                toPlaylistScreen = { url -> navController.navigate(Tabs.Search.Playlist(url)) },
            )
        }
        composable<Tabs.Search.Channel> {
            ChannelScreen(
                url = it.toRoute<Tabs.Search.Channel>().url,
                navigateBack = { navController.popBackStack() },
                toPlaylistScreen = { url -> navController.navigate(Tabs.Search.Playlist(url)) },
            )
        }
        composable<Tabs.Search.Playlist>(
            typeMap = mapOf(
                typeOf<PlaylistItem>() to OpenStreamNavTypes.playlistType,
            ),
        ) {
            PlaylistScreen(
                playlist = it.toRoute<Tabs.Search.Playlist>().playlist,
                toChannelScreen = { url -> navController.navigate(Tabs.Search.Channel(url)) },
                navigateBack = { navController.navigateUp() },
            )
        }
    }
}
