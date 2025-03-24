package com.github.freetube.core.media3.di

import com.github.freetube.core.media3.LibreTubeMediaPlayer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val media3Module = module {
    singleOf(::LibreTubeMediaPlayer)
}
