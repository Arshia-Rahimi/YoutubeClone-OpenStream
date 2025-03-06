package com.github.freetube.core.data.di

import com.github.freetube.core.data.LibreTubeDataRepository
import com.github.freetube.core.data.imp.PDLibreTubeDataRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::PDLibreTubeDataRepository) {
        bind<LibreTubeDataRepository>()
    }
}
