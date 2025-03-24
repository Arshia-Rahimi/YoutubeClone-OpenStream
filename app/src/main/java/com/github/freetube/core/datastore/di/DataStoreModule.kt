package com.github.freetube.core.datastore.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataStoreModule = module {
    singleOf(::LibreTubeDataStore)
}
