package com.github.arshiarahimi.openstream.ui.feature.subscriptions

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.data.ChannelRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SubscriptionsViewModel(
    private val channelRepo: ChannelRepository,
) : ViewModel() {

    val videos: SnapshotStateList<DataItem> = mutableStateListOf<DataItem>()
        .apply {
            channelRepo.subscribedVideos.onEach {
                val sortedVideos = it.sortedBy { it.uploadDate }
                videos.clear()
                videos.addAll(sortedVideos)
            }.launchIn(viewModelScope)
        }

}
