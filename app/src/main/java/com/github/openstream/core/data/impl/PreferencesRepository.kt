package com.github.openstream.core.data.impl

import com.github.openstream.core.datastore.PreferencesModel
import com.github.openstream.ui.feature.library.components.SortType
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesRepository {
    val preferences: SharedFlow<PreferencesModel>

    suspend fun setLibrarySortType(sortType: SortType)
}
