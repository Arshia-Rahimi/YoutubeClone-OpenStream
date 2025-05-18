package com.github.arshiarahimi.openstream.core.media3.di

import com.github.arshiarahimi.openstream.core.media3.OpenStreamMediaPlayer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val media3Module = module {
    singleOf(::OpenStreamMediaPlayer)
}
