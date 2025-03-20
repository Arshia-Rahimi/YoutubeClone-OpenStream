package com.github.freetube.ui.di

import com.github.freetube.ui.feature.downloads.DownloadsScreenModel
import com.github.freetube.ui.feature.library.LibraryScreenModel
import com.github.freetube.ui.feature.search.SearchScreenModel
import com.github.freetube.ui.feature.settings.SettingsScreenModel
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreenModel
import com.github.freetube.ui.global.player.PlayerViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {
    factoryOf(::SubscriptionsScreenModel)
    factoryOf(::SettingsScreenModel)
    factoryOf(::DownloadsScreenModel)
    factoryOf(::LibraryScreenModel)
    factoryOf(::SearchScreenModel)
    single { PlayerViewModel() }
}
