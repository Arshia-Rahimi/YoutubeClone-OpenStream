package com.github.arshiarahimi.openstream.ui.feature.subscriptions

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.util.next
import com.github.arshiarahimi.openstream.core.data.PreferencesRepository
import com.github.arshiarahimi.openstream.core.model.enums.SubscriptionsSortType
import com.github.arshiarahimi.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SubscriptionsViewModel(
//    private val channelRepo: ChannelRepository,
    private val preferencesRepo: PreferencesRepository,
) : ViewModel() {

    val sortType = preferencesRepo.preferences
        .map { it.subscriptionsSortType }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SubscriptionsSortType.DATE_ADDED_ASC,
        )

    val playlists = mutableStateListOf<DataItem>()
        .apply {
//            channelRepo.subscriptions
//                .combine(sortType) { newPlaylists, sortType ->
//                    val sortedChannels = when (sortType) {
//                        SubscriptionsSortType.DATE_ADDED_DESC -> newPlaylists
//                        SubscriptionsSortType.DATE_ADDED_ASC -> newPlaylists.reversed()
//                        SubscriptionsSortType.MOST_SUBSCRIBERS -> newPlaylists.sortedBy { it.subscriberCount }
//                        SubscriptionsSortType.FEWEST_SUBSCRIBERS -> newPlaylists.sortedByDescending { it.subscriberCount }
//                    }
//                    clear()
//                    addAll(sortedChannels)
//                }.launchIn(viewModelScope)
        }

    fun toggleSortType() {
        viewModelScope.launch {
            preferencesRepo.setSubscriptionsSortType(sortType.value.next())
        }
    }

}
