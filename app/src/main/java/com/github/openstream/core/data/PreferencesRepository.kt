package com.github.openstream.core.data

import com.github.openstream.ui.feature.library.components.SortType
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val librarySortType: Flow<SortType>
    
    suspend fun setLibrarySortType(sortType: SortType)
}
