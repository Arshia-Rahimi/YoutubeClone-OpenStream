package com.github.freetube.ui.di

import com.github.freetube.ui.feature.downloads.DownloadsViewModel
import com.github.freetube.ui.feature.library.LibraryViewModel
import com.github.freetube.ui.feature.search.SearchViewModel
import com.github.freetube.ui.feature.settings.SettingsViewModel
import com.github.freetube.ui.feature.subscriptions.SubscriptionsViewModel
import com.github.freetube.ui.global.channel.ChannelViewModel
import com.github.freetube.ui.global.player.PlayerViewModel
import com.github.freetube.ui.global.playlist.PlaylistViewModel
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
    singleOf(::PlayerViewModel)
}
