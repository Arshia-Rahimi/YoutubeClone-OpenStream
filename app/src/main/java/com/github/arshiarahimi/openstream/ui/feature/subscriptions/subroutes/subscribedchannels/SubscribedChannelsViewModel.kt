package com.github.arshiarahimi.openstream.ui.feature.subscriptions.subroutes.subscribedchannels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.compose.SnackBarController
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.data.ChannelRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SubscribedChannelsViewModel(
    private val channelRepo: ChannelRepository,
) : ViewModel() {

    val subscriptions: SnapshotStateList<DataItem> = mutableStateListOf<DataItem>().apply {
        channelRepo.subscriptions.onEach {
            val sortedList = it.sortedBy { it.name }
            subscriptions.clear()
            subscriptions.addAll(sortedList)
        }.launchIn(viewModelScope)
    }

    fun unSubscribe(channel: ChannelItem.OfflineFirstChannelItem) {
        channelRepo.unSubscribe(channel.id).onEach {
            when (it) {
                is Resource.Success -> SnackBarController.sendEvent("unsubscribed from ${channel.name}")
                is Resource.Error -> SnackBarController.sendEvent("failed to unsubscribe from ${channel.name}")
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

}
