package com.github.freetube.core.data.imp

import com.github.freetube.core.data.SettingsRepository
import com.github.freetube.core.datastore.LibreTubeDataStore
import com.github.freetube.core.model.AppTheme
import com.github.freetube.core.model.LibreTubeSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PDSettingsRepository(
    private val dataStore: LibreTubeDataStore,
): SettingsRepository {
    
    override val settings = dataStore.data
        .map {
            LibreTubeSettings(
                appTheme = it.appTheme,
            )
        }

    override suspend fun setAppTheme(appTheme: AppTheme) = withContext(Dispatchers.IO) {
        dataStore.setAppTheme(appTheme)
    }
}
