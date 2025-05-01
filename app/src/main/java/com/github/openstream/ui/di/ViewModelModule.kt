package com.github.openstream.ui.di

import com.github.openstream.ui.feature.downloads.DownloadsViewModel
import com.github.openstream.ui.feature.library.LibraryViewModel
import com.github.openstream.ui.feature.search.SearchViewModel
import com.github.openstream.ui.feature.settings.SettingsViewModel
import com.github.openstream.ui.feature.subscriptions.SubscriptionsViewModel
import com.github.openstream.ui.global.navigation.NavigationViewModel
import com.github.openstream.ui.global.player.PlayerViewModel
import com.github.openstream.ui.global.reusable.channel.ChannelViewModel
import com.github.openstream.ui.global.reusable.playlist.PlaylistViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SubscriptionsViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::DownloadsViewModel)
    viewModelOf(::LibraryViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::ChannelViewModel)
    viewModelOf(::PlaylistViewModel)
    viewModelOf(::NavigationViewModel)
    singleOf(::PlayerViewModel)
}
