package com.github.openstream.ui.feature.search.navigation

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
import com.github.openstream.core.model.extractordata.PlaylistItem
import com.github.openstream.ui.feature.search.SearchScreen
import com.github.openstream.ui.global.player.PlayerViewModel
import com.github.openstream.ui.global.screens.channel.ChannelScreen
import com.github.openstream.ui.global.screens.playlist.PlaylistScreen
import kotlin.reflect.typeOf

@Composable
fun SearchNavHost(
    navViewModel: NavigationViewModel,
    playerViewModel: PlayerViewModel,
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

    NavHost(
        navController = navController,
        startDestination = Tabs.Search.Root,
    ) {
        composable<Tabs.Search.Root> {
            SearchScreen(
                topBar = navViewModel::setTopBar,
                toChannelScreen = { navController.navigate(Tabs.Search.Channel(it)) },
                toPlaylistScreen = { navController.navigate(Tabs.Search.Playlist(it)) },
                playVideo = playerViewModel::start,
            )
        }
        composable<Tabs.Search.Channel> {
            ChannelScreen(
                url = it.toRoute<Tabs.Search.Channel>().url,
                topBar = navViewModel::setTopBar,
                navigateBack = { navController.popBackStack() },
                toPlaylistScreen = { navController.navigate(Tabs.Search.Playlist(it)) },
                playVideo = playerViewModel::start,
            )
        }
        composable<Tabs.Search.Playlist>(
            typeMap = mapOf(
                typeOf<PlaylistItem>() to OpenStreamNavTypes.playlistType,
            ),
        ) {
            PlaylistScreen(
                playlist = it.toRoute<Tabs.Search.Playlist>().playlist,
                topBar = navViewModel::setTopBar,
                toChannelScreen = { navController.navigate(Tabs.Search.Channel(it)) },
                playVideo = playerViewModel::start,
            )
        }
    }
}
