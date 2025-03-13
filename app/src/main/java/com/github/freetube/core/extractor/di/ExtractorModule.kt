package com.github.freetube.core.extractor.di

import com.github.freetube.core.extractor.KtorDownloader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val extractorModule = module {
    singleOf(::KtorDownloader)
}
