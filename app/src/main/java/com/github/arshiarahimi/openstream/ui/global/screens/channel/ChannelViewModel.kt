package com.github.arshiarahimi.openstream.ui.global.screens.channel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.replaceFirstWith
import com.github.arshiarahimi.openstream.core.data.ChannelRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.extractor.ChannelExtractor
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTabView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ChannelViewModel(
    private val url: String,
    private val channelRepo: ChannelRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val channelExtractor: ChannelExtractor) : UiState
    }

    private val channelExtractor: ChannelExtractor
        get() = (_uiState.value as UiState.Success).channelExtractor
    val channelItem: ChannelItem = channelExtractor.channelItem

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()
        .apply {
            channelRepo.getChannel(url).onEach {
                _uiState.value = when (it) {
                    is Resource.Error -> UiState.Error(it.message)
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(it.data)
                }
            }.launchIn(viewModelScope)
        }

    val tabs = mutableStateListOf<ChannelTabView>()
    val tabItems = mutableStateListOf<SnapshotStateList<DataItem>>()

    fun getTabFirstPage(tab: ChannelTabView) {
        if (_uiState.value !is UiState.Success) return
        viewModelScope.launch {
            channelRepo.getTabFirstPage(
                channelItem,
                channelExtractor,
                tab.toChannelTab()
            )
                .collect {
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
                            val index = tabs.indexOfFirst { it == tab }
                            tabItems[index].addAll(it.data ?: emptyList())
                        }
                    }
                }
        }
    }

    fun getTabNextPage(tab: ChannelTabView) {
        if (_uiState.value !is UiState.Success) return
        viewModelScope.launch {
            channelRepo.getTabNextPage(
                channelItem,
                channelExtractor,
                tab.toChannelTab()
            ).collect {
                when (it) {
                    is Resource.Success -> {
                        val index = tabs.indexOfFirst { it == tab }
                        tabItems[index].addAll(it.data ?: emptyList())
                    }

                    else -> Unit
                }
            }
        }
    }
}
