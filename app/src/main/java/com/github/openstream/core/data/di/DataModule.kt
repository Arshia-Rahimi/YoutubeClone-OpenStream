package com.github.openstream.core.data.di

import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlayerConfigRepository
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.data.impl.DataStorePlayerConfigRepository
import com.github.openstream.core.data.impl.DataStorePreferencesRepository
import com.github.openstream.core.data.impl.ExtractorChannelRepository
import com.github.openstream.core.data.impl.ExtractorSearchRepository
import com.github.openstream.core.data.impl.ExtractorVideoRepository
import com.github.openstream.core.data.impl.OfflineFirstPlaylistRepository
import com.github.openstream.core.data.impl.PreferencesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    factoryOf(::ExtractorSearchRepository) { bind<SearchRepository>() }

    factoryOf(::ExtractorChannelRepository) { bind<ChannelRepository>() }

    factoryOf(::ExtractorVideoRepository) { bind<VideoRepository>() }

    factoryOf(::OfflineFirstPlaylistRepository) { bind<PlaylistRepository>() }

    factoryOf(::DataStorePreferencesRepository) { bind<PreferencesRepository>() }

    factoryOf(::DataStorePlayerConfigRepository) { bind<PlayerConfigRepository>() }
}
