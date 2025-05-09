package com.github.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.openstream.core.datastore.PreferencesModel
import com.github.openstream.ui.feature.library.components.SortType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn

class DataStorePreferencesRepository(
    private val dataStore: DataStore<PreferencesModel>,
    private val scope: CoroutineScope,
) : PreferencesRepository {
    override val preferences = dataStore.data
        .shareIn(
            scope = scope,
            started = SharingStarted.Lazily,
            replay = 1,
        )

    override suspend fun setLibrarySortType(sortType: SortType) {
        dataStore.updateData { preferences.first().copy(librarySortType = sortType) }
    }
}
