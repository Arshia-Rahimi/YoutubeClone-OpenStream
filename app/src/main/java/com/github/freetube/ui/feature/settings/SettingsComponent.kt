package com.github.freetube.ui.feature.settings

import com.arkivanov.decompose.ComponentContext
import com.github.freetube.app.PageComponent

class SettingsComponent(
    componentContext: ComponentContext,
//    private val settingsRepository: SettingsRepository,
) : PageComponent, ComponentContext by componentContext {
//    
//    val settingsData = settingsRepository.settings
//        .stateIn(
//            scope = ioContext,
//            initialValue = LibreTubeSettings(),
//            started = SharingStarted.WhileSubscribed(5000L)
//        )
//
//    fun changeAppTheme() = screenModelScope.launch {
//        settingsRepository.setAppTheme(settingsData.value.appTheme.next())
//    }
}
