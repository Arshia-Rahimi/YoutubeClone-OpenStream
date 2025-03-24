package com.github.freetube.core.data.di

import com.github.freetube.core.data.ChannelRepository
import com.github.freetube.core.data.SearchRepository
import com.github.freetube.core.data.imp.DataStoreRepository
import com.github.freetube.core.data.imp.ExtractorChannelRepository
import com.github.freetube.core.data.imp.ExtractorSearchRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::DataStoreRepository)

    factoryOf(::ExtractorSearchRepository) {
        bind<SearchRepository>()
    }

    factoryOf(::ExtractorChannelRepository) {
        bind<ChannelRepository>()
    }
}
