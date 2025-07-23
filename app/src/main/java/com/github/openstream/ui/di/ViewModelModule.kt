package com.github.openstream.ui.di

import com.github.openstream.ui.feature.library.root.LibraryViewModel
import com.github.openstream.ui.feature.search.root.SearchViewModel
import com.github.openstream.ui.feature.settings.log.LogViewModel
import com.github.openstream.ui.feature.settings.root.SettingsViewModel
import com.github.openstream.ui.feature.subscriptions.root.SubscriptionsViewModel
import com.github.openstream.ui.global.player.PlayerViewModel
import com.github.openstream.ui.global.reusable.popups.addtoplaylistmodal.SaveVideoToPlaylistsViewModel
import com.github.openstream.ui.global.reusable.popups.confirmationdialog.ConfirmationDialogViewModel
import com.github.openstream.ui.global.reusable.popups.inputdialog.InputDialogViewModel
import com.github.openstream.ui.global.reusable.screens.channel.ChannelViewModel
import com.github.openstream.ui.global.reusable.screens.playlist.PlaylistViewModel
import com.github.openstream.ui.navigation.NavigationViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    // global
    singleOf(::NavigationViewModel)
    singleOf(::PlayerViewModel)
    
    // tabs
    viewModelOf(::SearchViewModel)
    
    viewModelOf(::LibraryViewModel)
    
    viewModelOf(::SubscriptionsViewModel)
    
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LogViewModel)
    

    // reusable
    //// screens
    viewModelOf(::ChannelViewModel)
    viewModelOf(::PlaylistViewModel)
    
    //// components
    viewModelOf(::SaveVideoToPlaylistsViewModel)
    viewModelOf(::InputDialogViewModel)
    viewModelOf(::ConfirmationDialogViewModel)
}
