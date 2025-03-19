package com.github.freetube.ui.feature.settings

//class SettingsScreenModel(
//    private val settingsRepository: SettingsRepository,
//) : ScreenModel {
//
//    val settingsData = settingsRepository.settings
//        .stateIn(
//            scope = screenModelScope,
//            initialValue = LibreTubeSettings(),
//            started = SharingStarted.WhileSubscribed(5000L)
//        )
//
//    fun changeAppTheme() = screenModelScope.launch { 
//        settingsRepository.setAppTheme(settingsData.value.appTheme.next())
//    }
//    
//}
