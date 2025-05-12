package com.github.openstream.ui.global.screens.channel

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.extractor.ChannelExtractor
import com.github.openstream.core.model.extractordata.ChannelMetadata
import com.github.openstream.core.model.extractordata.ChannelTab
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChannelViewModel(
    private val url: String,
    private val channelRepository: ChannelRepository,
) : ViewModel() {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val channelInfo: ChannelMetadata) : UiState
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    
    private lateinit var channel: ChannelExtractor

    private val loadingChannel: Job

    @SuppressLint("MutableCollectionMutableState")
    val tabResults = mutableStateOf(emptyList<MutableState<ChannelTab>>())
    val tabItems = mutableStateListOf<SnapshotStateList<DataItem>>()

    init {
        loadingChannel = viewModelScope.launch {
            channelRepository.getChannelData(url)
                .collect {
                    _uiState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> {
                            UiState.Error(it.message)
                        }
                        is Resource.Success -> {
                            channel = it.data
                            repeat(it.data.data.tabs.size) { tabItems.add(mutableStateListOf()) }
                            tabResults.value = it.data.data.tabs.map { mutableStateOf(it) }
                            UiState.Success(it.data.data)
                        }
                    }
                }
        }
    }

    fun onAction(action: ChannelAction) {
        val tab = tabResults.value[action.tab].value
        when (action) {
            is ChannelAction.GetTab -> getTab(tab, action.index)
            is ChannelAction.GetTabNextPage -> getTabNextPage(tab, action.index)
        }
    }

    private fun getTab(tab: ChannelTab, index: Int) {
        viewModelScope.launch {
            loadingChannel.join()
            channelRepository.getTab(tab, channel)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Error -> {
                            tabResults.value[index].value = tabResults.value[index].value
                                .copy(isLoading = false, error = it.message)
                        }
                        is Resource.Success -> {
                            tabResults.value[index].value =
                                tabResults.value[index].value.copy(isLoading = false)
                            tabItems[index].addAll(it.data ?: emptyList())
                        }
                    }
                }
        }
    }

    private fun getTabNextPage(tab: ChannelTab, index: Int) {
        viewModelScope.launch {
            if (channel.tabExtractors[index].third == null) return@launch
            channelRepository.getTabNextPage(tab, channel)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Error -> {
                            tabResults.value[index].value = tabResults.value[index].value
                                .copy(isLoading = false, error = it.message)
                        }
                        is Resource.Success -> {
                            tabResults.value[index].value =
                                tabResults.value[index].value.copy(isLoading = false)
                            tabItems[index].addAll(it.data ?: emptyList())
                        }
                    }
                }
        }
    }
}
