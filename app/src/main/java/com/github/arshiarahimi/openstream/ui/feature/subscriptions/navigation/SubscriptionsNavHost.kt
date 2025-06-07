package com.github.arshiarahimi.openstream.ui.feature.subscriptions.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.arshiarahimi.openstream.app.navigation.NavigationViewModel
import com.github.arshiarahimi.openstream.app.navigation.isInTabRoot
import com.github.arshiarahimi.openstream.app.navigation.routes.OpenStreamNavTypes
import com.github.arshiarahimi.openstream.app.navigation.routes.Tabs
import com.github.arshiarahimi.openstream.core.common.compose.popToRoot
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.ui.feature.subscriptions.SubscriptionsScreen
import com.github.arshiarahimi.openstream.ui.global.player.PlayerViewModel
import com.github.arshiarahimi.openstream.ui.global.screens.channel.ChannelScreen
import com.github.arshiarahimi.openstream.ui.global.screens.playlist.PlaylistScreen
import kotlin.reflect.typeOf

@Composable
fun SubscriptionsNavHost(
    navViewModel: NavigationViewModel,
    playerViewModel: PlayerViewModel,
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        navViewModel.tabClickAction
            .collect {
                if (it == Tabs.Subscriptions) {
                    if (!navController.isInTabRoot()) {
                        navController.popToRoot()
                    }
                }
            }
    }

    NavHost(
        navController = navController,
        startDestination = Tabs.Subscriptions.Root,
    ) {
        composable<Tabs.Subscriptions.Root> {
            SubscriptionsScreen(
                toChannelScreen = { navController.navigate(Tabs.Subscriptions.Channel(it)) },
                toPlaylistScreen = { navController.navigate(Tabs.Subscriptions.Playlist(it)) },
                playVideo = playerViewModel::start,
            )
        }
        composable<Tabs.Subscriptions.Channel> {
            ChannelScreen(
                url = it.toRoute<Tabs.Subscriptions.Channel>().url,
                playVideo = playerViewModel::start,
                navigateBack = { navController.popBackStack() },
                toPlaylistScreen = { navController.navigate(Tabs.Subscriptions.Playlist(it)) },
            )
        }
        composable<Tabs.Subscriptions.Playlist>(
            typeMap = mapOf(
                typeOf<PlaylistItem>() to OpenStreamNavTypes.playlistType,
            ),
        ) {
            PlaylistScreen(
                playlist = it.toRoute<Tabs.Subscriptions.Playlist>().playlist,
                playVideo = playerViewModel::start,
                toChannelScreen = { navController.navigate(Tabs.Subscriptions.Channel(it)) },
                navigateBack = { navController.navigateUp() },
            )
        }
    }
}
