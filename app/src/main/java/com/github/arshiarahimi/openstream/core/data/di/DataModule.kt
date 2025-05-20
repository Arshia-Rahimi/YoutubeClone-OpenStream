package com.github.arshiarahimi.openstream.core.data.di

import com.github.arshiarahimi.openstream.core.data.ChannelRepository
import com.github.arshiarahimi.openstream.core.data.PlayerConfigRepository
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.core.data.PreferencesRepository
import com.github.arshiarahimi.openstream.core.data.SearchRepository
import com.github.arshiarahimi.openstream.core.data.VideoRepository
import com.github.arshiarahimi.openstream.core.data.impl.DataStorePlayerConfigRepository
import com.github.arshiarahimi.openstream.core.data.impl.DataStorePreferencesRepository
import com.github.arshiarahimi.openstream.core.data.impl.ExtractorVideoRepository
import com.github.arshiarahimi.openstream.core.data.impl.OfflineFirstChannelRepository
import com.github.arshiarahimi.openstream.core.data.impl.OfflineFirstPlaylistRepository
import com.github.arshiarahimi.openstream.core.data.impl.OnlineSearchRepository
import com.github.arshiarahimi.openstream.core.shared.PLAYER_CONFIG_QUALIFIER
import com.github.arshiarahimi.openstream.core.shared.PREFERENCES_QUALIFIER
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module

val dataModule = module {
    factoryOf(::OnlineSearchRepository) { bind<SearchRepository>() }
    
    factoryOf(::OfflineFirstChannelRepository) { bind<ChannelRepository>() }

    factoryOf(::ExtractorVideoRepository) { bind<VideoRepository>() }

    factoryOf(::OfflineFirstPlaylistRepository) { bind<PlaylistRepository>() }

    factory {
        DataStorePreferencesRepository(
            dataStore = get(named(PREFERENCES_QUALIFIER)),
            scope = get()
        )
    } binds arrayOf(PreferencesRepository::class)

    factory {
        DataStorePlayerConfigRepository(
            dataStore = get(named(PLAYER_CONFIG_QUALIFIER)),
        )
    } binds arrayOf(PlayerConfigRepository::class)

}
