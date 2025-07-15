package com.github.openstream.ui.feature.settings

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
import com.github.openstream.ui.feature.settings.root.SettingsScreen
import com.github.openstream.ui.global.screens.channel.ChannelScreen
import com.github.openstream.ui.global.screens.playlist.PlaylistScreen
import kotlin.reflect.typeOf

@Composable
fun SettingsNavHost(
    navViewModel: NavigationViewModel,
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        navViewModel.tabClickAction
            .collect {
                if (it == Tabs.Settings) {
                    if (!navController.isInTabRoot()) {
                        navController.popToRoot()
                    }
                }
            }
    }
    LaunchedEffect(Unit) {
        navViewModel.navigateToChannelScreenFromSheetEvent
            .collect {
                if (it.first == Tabs.Settings)
                    navController.navigate(Tabs.Settings.Channel(it.second))
            }
    }

    NavHost(
        navController = navController,
        startDestination = Tabs.Settings.Root,
    ) {
        composable<Tabs.Settings.Root> {
            SettingsScreen()
        }
        composable<Tabs.Settings.Channel> {
            ChannelScreen(
                url = it.toRoute<Tabs.Settings.Channel>().url,
                navigateBack = { navController.popBackStack() },
                toPlaylistScreen = { url -> navController.navigate(Tabs.Settings.Playlist(url)) },
            )
        }
        composable<Tabs.Settings.Playlist>(
            typeMap = mapOf(
                typeOf<PlaylistItem>() to OpenStreamNavTypes.playlistType,
            ),
        ) {
            PlaylistScreen(
                playlist = it.toRoute<Tabs.Settings.Playlist>().playlist,
                toChannelScreen = { url -> navController.navigate(Tabs.Settings.Channel(url)) },
                navigateBack = { navController.navigateUp() },
            )
        }
    }
}
