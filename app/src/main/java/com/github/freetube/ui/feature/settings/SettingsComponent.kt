package com.github.freetube.ui.feature.settings

import com.arkivanov.decompose.ComponentContext
import com.github.freetube.ui.designsystem.TabComponent

class SettingsComponent(
    componentContext: ComponentContext,
//    private val settingsRepository: SettingsRepository,
) : TabComponent, ComponentContext by componentContext {
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
