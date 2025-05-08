package com.github.openstream.core.data.imp

import androidx.datastore.core.DataStore
import com.github.openstream.core.datastore.PlayerConfigModel
import com.github.openstream.core.datastore.PreferencesModel
import com.github.openstream.ui.feature.library.components.SortType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn

class PreferencesRepository(
    private val preferencesDataStore: DataStore<PreferencesModel>,
    private val playerConfigDataStore: DataStore<PlayerConfigModel>,
    private val scope: CoroutineScope,
) {

    val playerConfig = playerConfigDataStore.data
    val preferences = preferencesDataStore.data
        .shareIn(
            scope = scope,
            started = SharingStarted.Lazily,
            replay = 1,
        )

    suspend fun setLibrarySortType(sortType: SortType) {
        preferencesDataStore.updateData { preferences.first().copy(librarySortType = sortType) }
    }

    suspend fun updatePlayerConfig(config: PlayerConfigModel) {
        playerConfigDataStore.updateData { config } 
    }
    
}
