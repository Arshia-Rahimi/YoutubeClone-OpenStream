package com.github.arshiarahimi.openstream.ui.feature.subscriptions

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.compose.SnackBarController
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.data.ChannelRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SubscriptionsViewModel(
    private val channelRepo: ChannelRepository,
) : ViewModel() {

    val isRefreshing = MutableStateFlow(false)

    val videos: SnapshotStateList<DataItem> = mutableStateListOf<DataItem>()
        .apply {
            channelRepo.subscribedVideos.onEach {
                val sortedVideos = it.sortedBy { it.uploadDate }
                videos.clear()
                videos.addAll(sortedVideos)
            }.launchIn(viewModelScope)
        }

    fun updateSubscriptions() {
        viewModelScope.launch {
            channelRepo.updateSubscriptions().collect {
                when (it) {
                    is Resource.Loading -> isRefreshing.value = true
                    is Resource.Error -> {
                        isRefreshing.value = false
                        SnackBarController.sendEvent("failed to update subscriptions")
                    }

                    is Resource.Success -> isRefreshing.value = false
                }
            }
        }
    }
}
