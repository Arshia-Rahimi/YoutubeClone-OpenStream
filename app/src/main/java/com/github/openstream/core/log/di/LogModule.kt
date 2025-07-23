package com.github.openstream.core.log.di

import com.github.openstream.BuildConfig
import com.github.openstream.core.log.DebugLogger
import com.github.openstream.core.log.Logger
import com.github.openstream.core.log.OpenStreamLogger
import org.koin.dsl.module

val logModule = module {
    
    single<Logger> {
        if (BuildConfig.DEBUG) DebugLogger()
        else OpenStreamLogger(get(), get())
    }
}
