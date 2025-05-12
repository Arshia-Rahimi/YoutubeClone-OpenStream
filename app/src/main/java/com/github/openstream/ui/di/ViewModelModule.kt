package com.github.openstream.ui.di

import com.github.openstream.app.navigation.NavigationViewModel
import com.github.openstream.ui.feature.downloads.DownloadsViewModel
import com.github.openstream.ui.feature.library.LibraryViewModel
import com.github.openstream.ui.feature.search.SearchViewModel
import com.github.openstream.ui.feature.settings.SettingsViewModel
import com.github.openstream.ui.feature.subscriptions.SubscriptionsViewModel
import com.github.openstream.ui.global.player.PlayerViewModel
import com.github.openstream.ui.global.popups.addtoplaylistmodal.SaveVideoToPlaylistsViewModel
import com.github.openstream.ui.global.popups.createplaylistdialog.CreatePlaylistViewModel
import com.github.openstream.ui.global.popups.unsubscribe.UnsubscribeViewModel
import com.github.openstream.ui.global.screens.channel.ChannelViewModel
import com.github.openstream.ui.global.screens.playlist.PlaylistViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    // tabs
    viewModelOf(::SubscriptionsViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::DownloadsViewModel)
    viewModelOf(::LibraryViewModel)
    viewModelOf(::SearchViewModel)

    // reusable
    viewModelOf(::ChannelViewModel)
    viewModelOf(::PlaylistViewModel)

    // global
    singleOf(::NavigationViewModel)
    singleOf(::PlayerViewModel)

    // components
    viewModelOf(::SaveVideoToPlaylistsViewModel)
    viewModelOf(::CreatePlaylistViewModel)
    viewModelOf(::UnsubscribeViewModel)
}
