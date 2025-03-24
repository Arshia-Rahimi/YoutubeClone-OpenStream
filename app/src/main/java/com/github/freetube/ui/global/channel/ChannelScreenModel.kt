package com.github.freetube.ui.global.channel

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.ChannelRepository
import com.github.freetube.core.extractor.channel.ChannelInfo
import com.github.freetube.core.extractor.channel.ChannelTab
import com.github.freetube.core.extractor.channel.ChannelUnit
import com.github.freetube.core.extractor.model.DataItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChannelScreenModel(
    private val url: String,
    private val channelRepository: ChannelRepository,
) : StateScreenModel<ChannelScreenModel.UiState>(UiState.Loading) {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val channelInfo: ChannelInfo) : UiState
    }

    private lateinit var channel: ChannelUnit

    private val loadingChannel: Job

    @SuppressLint("MutableCollectionMutableState")
    val tabResults = mutableStateOf(emptyList<MutableState<ChannelTab>>())
    val tabItems = mutableStateListOf<SnapshotStateList<DataItem>>()

    init {
        loadingChannel = screenModelScope.launch {
            channelRepository.getChannelData(url)
                .collect {
                    mutableState.value = when (it) {
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
        screenModelScope.launch {
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
        screenModelScope.launch {
            // todo need to stop when reaching the end
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
