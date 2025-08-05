package com.github.openstream.core.datastore

import com.github.openstream.core.common.datastore.DataStoreModel
import com.github.openstream.core.shared.LibrarySortType
import kotlinx.serialization.Serializable

@Serializable
data class PreferencesModel(
    val librarySortType: LibrarySortType = LibrarySortType.CREATED_AT_ASC,
    val cookies: String? = null,
    val isAudioOnlyModeEnabled: Boolean = false,
): DataStoreModel()
