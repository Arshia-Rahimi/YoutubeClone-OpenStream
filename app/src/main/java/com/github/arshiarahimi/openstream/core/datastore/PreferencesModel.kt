package com.github.arshiarahimi.openstream.core.datastore

import com.github.arshiarahimi.openstream.core.model.enums.LibrarySortType
import com.github.arshiarahimi.openstream.core.model.enums.SubscriptionsSortType
import kotlinx.serialization.Serializable

@Serializable
data class PreferencesModel(
    val librarySortType: LibrarySortType = LibrarySortType.CREATED_AT_ASC,
    val subscriptionsSortType: SubscriptionsSortType = SubscriptionsSortType.DATE_ADDED_ASC,
)
