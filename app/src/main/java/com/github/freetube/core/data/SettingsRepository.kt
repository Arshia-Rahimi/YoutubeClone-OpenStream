package com.github.freetube.core.data

import com.github.freetube.core.model.AppTheme
import com.github.freetube.core.model.LibreTubeSettings
import kotlinx.coroutines.flow.Flow


interface SettingsRepository {
    val settings: Flow<LibreTubeSettings>
    
    suspend fun setAppTheme(appTheme: AppTheme)
}
