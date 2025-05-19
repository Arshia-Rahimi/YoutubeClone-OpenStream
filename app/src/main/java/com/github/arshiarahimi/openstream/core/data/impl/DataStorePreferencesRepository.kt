package com.github.arshiarahimi.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.arshiarahimi.openstream.core.data.PreferencesRepository
import com.github.arshiarahimi.openstream.core.datastore.PreferencesModel
import com.github.arshiarahimi.openstream.core.model.enums.LibrarySortType
import com.github.arshiarahimi.openstream.core.model.enums.SubscriptionsSortType
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

    override suspend fun setLibrarySortType(sortType: LibrarySortType) {
        dataStore.updateData { preferences.first().copy(librarySortType = sortType) }
    }

    override suspend fun setSubscriptionsSortType(sortType: SubscriptionsSortType) {
        dataStore.updateData { preferences.first().copy(subscriptionsSortType = sortType) }
    }
}
