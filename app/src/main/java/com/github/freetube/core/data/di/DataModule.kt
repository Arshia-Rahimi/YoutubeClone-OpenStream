package com.github.freetube.core.data.di

import com.github.freetube.core.data.ChannelRepository
import com.github.freetube.core.data.LibreTubeDataRepository
import com.github.freetube.core.data.SearchRepository
import com.github.freetube.core.data.SettingsRepository
import com.github.freetube.core.data.imp.ExtractorChannelRepository
import com.github.freetube.core.data.imp.ExtractorSearchRepository
import com.github.freetube.core.data.imp.PDLibreTubeDataRepository
import com.github.freetube.core.data.imp.PDSettingsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::PDLibreTubeDataRepository) {
        bind<LibreTubeDataRepository>()
    }

    singleOf(::PDSettingsRepository) {
        bind<SettingsRepository>()
    }

    singleOf(::ExtractorSearchRepository) {
        bind<SearchRepository>()
    }

    singleOf(::ExtractorChannelRepository) {
        bind<ChannelRepository>()
    }
}
