package com.github.openstream.core.extractor.di

import com.github.openstream.core.extractor.KtorDownloader
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.schabi.newpipe.extractor.downloader.Downloader

val extractorModule = module {
    singleOf(::KtorDownloader) {
        bind<Downloader>()
    }
}