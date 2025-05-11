package com.github.openstream.core.data.impl

import com.github.openstream.core.datastore.PreferencesModel
import com.github.openstream.core.model.enums.LibrarySortType
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesRepository {
    val preferences: SharedFlow<PreferencesModel>
    
    suspend fun setLibrarySortType(sortType: LibrarySortType)
}
