package com.github.freetube.ui.di

import com.github.freetube.ui.feature.home.HomeScreenViewModel
import com.github.freetube.ui.feature.settings.SettingsScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module { 
    viewModelOf(::HomeScreenViewModel) 
    viewModelOf(::SettingsScreenViewModel)
}
