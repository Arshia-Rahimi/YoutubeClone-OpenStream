package com.github.freetube.app.di

import com.github.freetube.app.MainActivityScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val mainModule = module {
    factoryOf(::MainActivityScreenModel)
    factory { CoroutineScope(Dispatchers.IO) }
}
