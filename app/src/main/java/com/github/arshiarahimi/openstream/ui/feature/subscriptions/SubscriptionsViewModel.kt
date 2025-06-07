package com.github.arshiarahimi.openstream.ui.feature.subscriptions

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.R
import com.github.arshiarahimi.openstream.core.common.compose.SnackBarController
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.data.ChannelRepository
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.shared.DefaultPlaylists
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SubscriptionsViewModel(
    private val channelRepo: ChannelRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    val isRefreshing = MutableStateFlow(false)

    val videos: SnapshotStateList<DataItem> = mutableStateListOf<DataItem>()
        .apply {
            channelRepo.subscribedVideos.onEach {
                val sortedVideos = it.sortedByDescending { it.uploadDate }
                videos.clear()
                videos.addAll(sortedVideos)
            }.launchIn(viewModelScope)
        }
    
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
    
    fun updateSubscriptions() {
        channelRepo.updateSubscriptions().onEach {
                when (it) {
                    is Resource.Loading -> isRefreshing.value = true
                    is Resource.Error -> {
                        isRefreshing.value = false
                        SnackBarController.sendEvent("failed to update subscriptions")
                    }

                    is Resource.Success -> isRefreshing.value = false
                }
        }.launchIn(viewModelScope)
    }

    fun addToWatchLater(video: VideoItem) {
        playlistRepo.addToPlaylist(listOf(video), DefaultPlaylists.WATCH_LATER_ID)
            .onEach {
                when (it) {
                    is Resource.Success -> {
                        SnackBarController.sendEvent(R.string.added_to_watch_later)
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }

}
