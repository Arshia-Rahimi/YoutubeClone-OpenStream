package com.github.openstream.core.data.di

import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.data.PreferencesRepository
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.data.imp.ExtractorChannelRepository
import com.github.openstream.core.data.imp.ExtractorSearchRepository
import com.github.openstream.core.data.imp.ExtractorVideoRepository
import com.github.openstream.core.data.imp.OfflineFirstPlaylistRepository
import com.github.openstream.core.data.imp.PDPreferencesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    factoryOf(::ExtractorSearchRepository) { bind<SearchRepository>() }

    factoryOf(::ExtractorChannelRepository) { bind<ChannelRepository>() }

    factoryOf(::ExtractorVideoRepository) { bind<VideoRepository>() }

    factoryOf(::OfflineFirstPlaylistRepository) { bind<PlaylistRepository>() }
    
    factoryOf(::PDPreferencesRepository) { bind<PreferencesRepository>() }
}
