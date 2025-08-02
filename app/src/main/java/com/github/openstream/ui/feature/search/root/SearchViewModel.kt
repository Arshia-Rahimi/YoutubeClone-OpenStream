package com.github.openstream.ui.feature.search.root

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.replaceFirstWith
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.shared.DefaultPlaylists
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.SearchResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SearchViewModel(
    private val searchRepo: SearchRepository,
    private val playlistRepo: PlaylistRepository,
    private val channelRepo: ChannelRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Empty : UiState
        data object Loading : UiState
        data class Error(val message: String?) : UiState
        data class Success(val searchResult: SearchResult) : UiState
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val uiState = _uiState.asStateFlow()
    val items = mutableStateListOf<DataItem>()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun search() {
        searchJob?.cancel()
        searchJob = searchRepo.search(searchQuery.value)
            .onEach {
                _uiState.value = when (it) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Error -> UiState.Error(it.error?.localizedMessage ?: "error")
                    is Resource.Success -> {
                        items.clear()
                        items.addAll(it.data.items)
                        UiState.Success(it.data)
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun getNextPage() {
        if (_uiState.value !is UiState.Success) return
        (_uiState.value as UiState.Success).searchResult.let {
            searchJob?.cancel()
            searchJob = searchRepo.getNextPage(it).onEach { result ->
                when (result) {
                    is Resource.Success -> items.addAll(result.data)
                    else -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    fun addToWatchLater(video: VideoItem) {
        playlistRepo.addToPlaylist(listOf(video), DefaultPlaylists.WATCH_LATER_ID)
            .onEach {
                when (it) {
                    is Resource.Success -> {
                        SnackBarController.sendEvent("added to watch later")
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)
    }

    fun savePlaylist(playlist: PlaylistItem.OnlinePlaylistItem) {
        playlistRepo.savePlaylist(playlist)
            .onEach {
                when (it) {
                    is Resource.Success -> SnackBarController.sendEvent("saved ${playlist.name}")
                    is Resource.Error -> SnackBarController.sendEvent("failed to save playlist ${playlist.name}")
                    is Resource.Loading -> Unit
                }
            }.launchIn(viewModelScope)
    }

    fun subscribe(channel: ChannelItem.OnlineChannelItem) {
        channelRepo.subscribe(channel).onEach { result ->
            when (result) {
                is Resource.Loading -> Unit
                is Resource.Error -> SnackBarController.sendEvent("failed to subscribe to channel")
                is Resource.Success -> {
                    if (uiState.value !is UiState.Success) return@onEach
                    val currentChannel =
                        (uiState.value as UiState.Success).searchResult.items
                            .filterIsInstance<ChannelItem>()
                            .first { it.url == channel.url } as ChannelItem
                    println(currentChannel)
                    println(result.data)
                    items.replaceFirstWith(result.data) {
                        it is ChannelItem && it.url == currentChannel.url
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun unsubscribe(channel: ChannelItem.OfflineFirstChannelItem) {
        channelRepo.unSubscribe(channel).onEach { result ->
            when (result) {
                is Resource.Loading -> Unit
                is Resource.Error -> SnackBarController.sendEvent("failed to subscribe to channel")
                is Resource.Success -> {
                    if (uiState.value !is UiState.Success) return@onEach
                    val currentChannel =
                        (uiState.value as UiState.Success).searchResult.items
                            .filterIsInstance<ChannelItem>()
                            .first { it.url == channel.url } as ChannelItem
                    
                    items.replaceFirstWith(result.data) {
                        it is ChannelItem && it.url == currentChannel.url
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}
