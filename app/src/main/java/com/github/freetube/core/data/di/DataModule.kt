package com.github.freetube.core.data.di

import com.github.freetube.core.data.ChannelRepository
import com.github.freetube.core.data.PlaylistRepository
import com.github.freetube.core.data.SearchRepository
import com.github.freetube.core.data.VideoRepository
import com.github.freetube.core.data.imp.ExtractorChannelRepository
import com.github.freetube.core.data.imp.ExtractorPlaylistRepository
import com.github.freetube.core.data.imp.ExtractorSearchRepository
import com.github.freetube.core.data.imp.ExtractorVideoRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    factoryOf(::ExtractorSearchRepository) { bind<SearchRepository>() }

    factoryOf(::ExtractorChannelRepository) { bind<ChannelRepository>() }

    factoryOf(::ExtractorVideoRepository) { bind<VideoRepository>() }

    factoryOf(::ExtractorPlaylistRepository) { bind<PlaylistRepository>() }
}
