package com.github.freetube.core.data.imp

import com.github.freetube.core.data.LibreTubeDataRepository
import com.github.freetube.core.datastore.LibreTubeDataStore
import com.github.freetube.core.model.AppTheme
import com.github.freetube.core.model.LibreTubeData
import kotlinx.coroutines.flow.map

class PDLibreTubeDataRepository(
    private val dataStore: LibreTubeDataStore
): LibreTubeDataRepository {
    
    override val data = dataStore.data
        .map {
            LibreTubeData(
                appTheme = it.appTheme,
            )
        }

    override suspend fun setAppTheme(appTheme: AppTheme) = 
        dataStore.setAppTheme(appTheme = appTheme)
}
