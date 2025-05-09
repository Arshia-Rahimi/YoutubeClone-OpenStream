package com.github.openstream.core.datastore

import com.github.openstream.ui.feature.library.components.SortType
import kotlinx.serialization.Serializable

@Serializable
data class PreferencesModel(
    val librarySortType: SortType = SortType.CREATED_AT_ASC,
)
