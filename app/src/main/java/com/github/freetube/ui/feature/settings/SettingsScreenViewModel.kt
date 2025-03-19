package com.github.freetube.ui.feature.settings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.next
import com.github.freetube.core.data.SettingsRepository
import com.github.freetube.core.model.LibreTubeSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val settingsRepository: SettingsRepository,
) : ScreenModel {

    val settingsData = settingsRepository.settings
        .stateIn(
            scope = screenModelScope,
            initialValue = LibreTubeSettings(),
            started = SharingStarted.WhileSubscribed(5000L)
        )

    fun changeAppTheme() = screenModelScope.launch { 
        settingsRepository.setAppTheme(settingsData.value.appTheme.next())
    }
    
}
