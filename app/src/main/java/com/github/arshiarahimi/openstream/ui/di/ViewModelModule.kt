package com.github.arshiarahimi.openstream.ui.di

import com.github.arshiarahimi.openstream.app.navigation.NavigationViewModel
import com.github.arshiarahimi.openstream.ui.feature.downloads.QueueViewModel
import com.github.arshiarahimi.openstream.ui.feature.library.LibraryViewModel
import com.github.arshiarahimi.openstream.ui.feature.search.SearchViewModel
import com.github.arshiarahimi.openstream.ui.feature.settings.SettingsViewModel
import com.github.arshiarahimi.openstream.ui.feature.subscriptions.SubscriptionsViewModel
import com.github.arshiarahimi.openstream.ui.feature.subscriptions.subroutes.subscribedchannels.SubscribedChannelsViewModel
import com.github.arshiarahimi.openstream.ui.global.player.PlayerViewModel
import com.github.arshiarahimi.openstream.ui.global.popups.addtoplaylistmodal.SaveVideoToPlaylistsViewModel
import com.github.arshiarahimi.openstream.ui.global.popups.confirmationdialog.ConfirmationDialogViewModel
import com.github.arshiarahimi.openstream.ui.global.popups.inputdialog.InputDialogViewModel
import com.github.arshiarahimi.openstream.ui.global.screens.channel.ChannelViewModel
import com.github.arshiarahimi.openstream.ui.global.screens.playlist.PlaylistViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    // tabs
    viewModelOf(::SubscriptionsViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::QueueViewModel)
    viewModelOf(::LibraryViewModel)
    viewModelOf(::SearchViewModel)

    // subRoutes
    viewModelOf(::SubscribedChannelsViewModel)

    // reusable
    viewModelOf(::ChannelViewModel)
    viewModelOf(::PlaylistViewModel)

    // global
    singleOf(::NavigationViewModel)
    singleOf(::PlayerViewModel)

    // components
    viewModelOf(::SaveVideoToPlaylistsViewModel)
    viewModelOf(::InputDialogViewModel)
    viewModelOf(::ConfirmationDialogViewModel)
}
