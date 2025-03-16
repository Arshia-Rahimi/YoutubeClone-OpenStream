package com.github.freetube.core.data.di

import com.github.freetube.core.data.LibreTubeDataRepository
import com.github.freetube.core.data.SettingsRepository
import com.github.freetube.core.data.YtRepository
import com.github.freetube.core.data.imp.ExtractorYtRepository
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

    singleOf(::ExtractorYtRepository) {
        bind<YtRepository>()
    }
}
