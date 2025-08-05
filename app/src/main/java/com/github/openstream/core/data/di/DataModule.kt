package com.github.openstream.core.data.di

import com.github.openstream.core.data.CacheRepository
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.data.PreferencesRepository
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.data.impl.DataStorePreferencesRepository
import com.github.openstream.core.data.impl.OfflineFirstChannelRepository
import com.github.openstream.core.data.impl.OfflineFirstPlaylistRepository
import com.github.openstream.core.data.impl.OfflineFirstVideoRepository
import com.github.openstream.core.data.impl.OnlineSearchRepository
import com.github.openstream.core.data.impl.OpenStreamCacheRepository
import com.github.openstream.core.datastore.PreferencesModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module

val dataModule = module {
    factoryOf(::OnlineSearchRepository) { bind<SearchRepository>() }
    
    factoryOf(::OfflineFirstChannelRepository) { bind<ChannelRepository>() }

    factoryOf(::OfflineFirstVideoRepository) { bind<VideoRepository>() }

    factoryOf(::OfflineFirstPlaylistRepository) { bind<PlaylistRepository>() }
    
    factoryOf(::OpenStreamCacheRepository) { bind<CacheRepository>() }

    factory {
        DataStorePreferencesRepository(
            dataStore = get(named(PreferencesModel.NAME)),
            scope = get(),
        )
    } binds arrayOf(PreferencesRepository::class)

}
