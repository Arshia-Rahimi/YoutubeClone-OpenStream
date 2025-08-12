package com.github.openstream.core.datastore

import com.github.openstream.core.shared.LibrarySortType
import kotlinx.serialization.Serializable

@Serializable
data class PreferencesModel(
    val librarySortType: LibrarySortType = LibrarySortType.CREATED_AT_ASC,
    val isAudioOnlyModeEnabled: Boolean = false,
) {
    companion object {
        const val NAME = "Preferences"
    }
}
