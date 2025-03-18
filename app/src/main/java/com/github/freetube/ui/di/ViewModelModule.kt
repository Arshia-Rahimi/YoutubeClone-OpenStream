package com.github.freetube.ui.di

import com.github.freetube.ui.feature.downloads.DownloadsScreenViewModel
import com.github.freetube.ui.feature.library.LibraryScreenViewModel
import com.github.freetube.ui.feature.search.main.SearchScreenViewModel
import com.github.freetube.ui.feature.settings.SettingsScreenViewModel
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreenViewModel
import com.github.freetube.ui.global.player.PlayerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module { 
    viewModelOf(::SubscriptionsScreenViewModel) 
    viewModelOf(::SettingsScreenViewModel)
    viewModelOf(::LibraryScreenViewModel)
    viewModelOf(::SearchScreenViewModel)
    viewModelOf(::DownloadsScreenViewModel)
    single { PlayerViewModel() }
}
