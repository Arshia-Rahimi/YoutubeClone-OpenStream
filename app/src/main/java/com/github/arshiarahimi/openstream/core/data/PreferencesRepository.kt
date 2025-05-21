package com.github.arshiarahimi.openstream.core.data

import com.github.arshiarahimi.openstream.core.datastore.PreferencesModel
import com.github.arshiarahimi.openstream.core.model.enums.LibrarySortType
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesRepository {
    val preferences: SharedFlow<PreferencesModel>

    suspend fun setLibrarySortType(sortType: LibrarySortType)
}
