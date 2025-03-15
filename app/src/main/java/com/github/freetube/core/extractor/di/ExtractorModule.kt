package com.github.freetube.core.extractor.di

import com.github.freetube.core.extractor.OkHttpDownloader
import org.koin.dsl.module

val extractorModule = module {
    single {
        OkHttpDownloader.init(null)
    }
}
