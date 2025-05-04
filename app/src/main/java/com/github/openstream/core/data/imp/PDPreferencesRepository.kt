package com.github.openstream.core.data.imp

import androidx.datastore.core.DataStore
import com.github.openstream.core.data.PreferencesRepository
import com.github.openstream.core.datastore.PreferencesModel
import com.github.openstream.ui.feature.library.components.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PDPreferencesRepository(
    private val dataStore: DataStore<PreferencesModel>,
): PreferencesRepository {
    
    override var librarySortType: Flow<SortType> = dataStore.data.map { it.librarySortType }
    
    private suspend fun update(transform: PreferencesModel.() -> PreferencesModel) =
        dataStore.updateData(transform)
    
    suspend fun setSeekIncrement(seekIncrement: Long) =
        update { copy(seekIncrement = seekIncrement) }
    
    override suspend fun setLibrarySortType(sortType: SortType) {
        update { copy(librarySortType = sortType) }
    }
    
}
