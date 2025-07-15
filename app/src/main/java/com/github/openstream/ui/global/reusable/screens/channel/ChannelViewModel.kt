package com.github.openstream.ui.global.reusable.screens.channel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.R
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.replaceFirstWith
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.shared.DefaultPlaylists
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.ChannelExtractor
import com.github.openstream.ui.global.reusable.screens.channel.model.ChannelTabView
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class ChannelViewModel(
    url: String,
    private val channelRepo: ChannelRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val channelExtractor: ChannelExtractor) : UiState
    }

    private val channelExtractor: ChannelExtractor
        get() = (uiState.value as UiState.Success).channelExtractor
    val channelItem: ChannelItem
        get() = channelExtractor.channelItem

    val uiState = channelRepo.getChannel(url)
        .map {
            when (it) {
                is Resource.Error -> UiState.Error(it.message)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> {
                    tabs.clear()
                    tabItems.clear()
                    tabs.addAll(
                        it.data.tabs
                            .map { ChannelTabView(it.name, it.url, true) }
                    )
                    repeat(it.data.tabs.size) {
                        tabItems.add(mutableStateListOf())
                    }
                    UiState.Success(it.data)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading,
        )

    val tabs = mutableStateListOf<ChannelTabView>()
    val tabItems = mutableStateListOf<SnapshotStateList<DataItem>>()

    fun getTabFirstPage(tab: ChannelTabView) {
        if (uiState.value !is UiState.Success) return

        val index = tabs.indexOfFirst { it.url == tab.url }
        // don't fetch first page for the second time
        // it's checked here for config changes
        if (tabItems[index].isNotEmpty()) return
        
        channelRepo.getTabFirstPage(channelItem, channelExtractor, tab.toChannelTab())
            .onEach {
                when (it) {
                    is Resource.Loading -> tabs.replaceFirstWith(tab.copy(isLoading = true)) { it == tab }
                    is Resource.Error ->
                        tabs.replaceFirstWith(
                            tab.copy(
                                isLoading = false,
                                error = it.message
                            )
                        ) { it == tab }

                    is Resource.Success -> {
                        tabs.replaceFirstWith(tab.copy(isLoading = false)) { it == tab }
                        tabItems[index].addAll(it.data ?: emptyList())
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun getTabNextPage(tab: ChannelTabView) {
        if (uiState.value !is UiState.Success) return
        channelRepo.getTabNextPage(channelItem, channelExtractor, tab.toChannelTab())
            .onEach {
                when (it) {
                    is Resource.Success -> {
                        val index = tabs.indexOfFirst { it.url == tab.url }
                        tabItems[index].addAll(it.data ?: emptyList())
                    }

                    else -> Unit
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

    fun savePlaylist(playlist: PlaylistItem.OnlinePlaylistItem) {
        playlistRepo.savePlaylist(playlist)
            .onEach {
                when (it) {
                    is Resource.Success -> SnackBarController.sendEvent("saved playlist ${playlist.name}")
                    is Resource.Error -> SnackBarController.sendEvent("failed to save playlist ${playlist.name}")
                    else -> Unit
                }
            }.launchIn(viewModelScope)
    }
}
