package com.github.openstream.core.log.di

import com.github.openstream.core.common.util.Logger
import com.github.openstream.core.log.OpenStreamLogger
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val logModule = module {
    singleOf(::OpenStreamLogger) { bind<Logger>() }
}
