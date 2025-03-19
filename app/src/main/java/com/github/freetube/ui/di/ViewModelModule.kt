package com.github.freetube.ui.di

import com.github.freetube.ui.global.player.PlayerViewModel
import org.koin.dsl.module

val viewModelModule = module {
//    viewModelOf(::SubscriptionsScreenViewModel) 
//    viewModelOf(::SettingsScreenViewModel)
//    viewModelOf(::LibraryScreenViewModel)
//    viewModelOf(::SearchScreenViewModel)
//    viewModelOf(::DownloadsScreenViewModel)
    single { PlayerViewModel() }
}
