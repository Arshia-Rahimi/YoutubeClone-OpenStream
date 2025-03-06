package com.github.freetube.core.datastore

import androidx.datastore.core.DataStore
import com.github.freetube.core.model.AppTheme
import kotlinx.coroutines.flow.map

class LibreTubeDataStore(
    private val dataStore: DataStore<LibreTubeDataStoreModel>
)  {

    val data = dataStore.data.map {
        LibreTubeDataStoreModel(
            appTheme = it.appTheme,
        )
    }

    private suspend fun update(transform: LibreTubeDataStoreModel.() -> LibreTubeDataStoreModel) {
        dataStore.updateData(transform)
    }

    suspend fun setAppTheme(appTheme: AppTheme) = update { copy(appTheme = appTheme) }

}
