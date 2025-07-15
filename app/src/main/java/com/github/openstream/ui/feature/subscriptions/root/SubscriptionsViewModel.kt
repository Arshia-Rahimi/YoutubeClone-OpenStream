package com.github.openstream.ui.feature.subscriptions.root

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.R
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.compose.collectToSnapShotStateList
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.shared.DefaultPlaylists
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SubscriptionsViewModel(
    private val channelRepo: ChannelRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    val isRefreshing = MutableStateFlow(false)

    val videos = channelRepo.subscribedVideos.collectToSnapShotStateList(viewModelScope) {
        it.sortedByDescending { video -> video.uploadDate }
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
