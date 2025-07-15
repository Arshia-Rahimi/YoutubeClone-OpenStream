package com.github.openstream.core.data

import com.github.openstream.core.datastore.PreferencesModel
import com.github.openstream.core.shared.enums.LibrarySortType
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesRepository {
    val preferences: SharedFlow<PreferencesModel>

    suspend fun setLibrarySortType(sortType: LibrarySortType)
}
