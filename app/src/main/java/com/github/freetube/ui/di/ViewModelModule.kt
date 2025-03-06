package com.github.freetube.ui.di

import com.github.freetube.ui.feature.main.MainScreenViewModel
import com.github.freetube.ui.feature.settings.SettingsScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module { 
    viewModelOf(::MainScreenViewModel) 
    viewModelOf(::SettingsScreenViewModel)
}
