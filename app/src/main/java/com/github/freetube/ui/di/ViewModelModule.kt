package com.github.freetube.ui.di

import com.github.freetube.ui.feature.playlists.PlaylistsScreenViewModel
import com.github.freetube.ui.feature.search.SearchScreenViewModel
import com.github.freetube.ui.feature.settings.SettingsScreenViewModel
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module { 
    viewModelOf(::SubscriptionsScreenViewModel) 
    viewModelOf(::SettingsScreenViewModel)
    viewModelOf(::PlaylistsScreenViewModel)
    viewModelOf(::SearchScreenViewModel)
}
