package com.github.freetube.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.freetube.core.common.util.next
import com.github.freetube.core.data.SettingsRepository
import com.github.freetube.core.model.LibreTubeSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val settingsRepository: SettingsRepository,
): ViewModel() {

    val settingsData = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            initialValue = LibreTubeSettings(),
            started = SharingStarted.WhileSubscribed(5000L)
        )
    
    fun changeAppTheme() = viewModelScope.launch { 
        settingsRepository.setAppTheme(settingsData.value.appTheme.next())
    }
    
}
