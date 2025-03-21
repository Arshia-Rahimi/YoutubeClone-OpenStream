package com.github.freetube.ui.di

import com.github.freetube.ui.designsystem.scaffold.ScaffoldScreenModel
import com.github.freetube.ui.feature.downloads.DownloadsScreenModel
import com.github.freetube.ui.feature.library.LibraryScreenModel
import com.github.freetube.ui.feature.search.main.SearchScreenModel
import com.github.freetube.ui.feature.settings.SettingsScreenModel
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreenModel
import com.github.freetube.ui.global.player.PlayerScreenModel
import com.github.freetube.ui.sharedscreens.channel.ChannelScreenModel
import com.github.freetube.ui.sharedscreens.playlist.PlaylistScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val screenModelModule = module {
    factoryOf(::SubscriptionsScreenModel)
    factoryOf(::SettingsScreenModel)
    factoryOf(::DownloadsScreenModel)
    factoryOf(::LibraryScreenModel)
    factoryOf(::SearchScreenModel)
    factoryOf(::ChannelScreenModel)
    factoryOf(::PlaylistScreenModel)
    singleOf(::PlayerScreenModel)
    singleOf(::ScaffoldScreenModel)
}
