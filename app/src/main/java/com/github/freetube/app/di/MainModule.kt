package com.github.freetube.app.di

import com.github.freetube.app.MainActivityViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::MainActivityViewModel)
}
