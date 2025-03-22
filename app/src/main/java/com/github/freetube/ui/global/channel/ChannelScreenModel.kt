package com.github.freetube.ui.global.channel

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.ChannelRepository
import com.github.freetube.core.extractor.channel.ChannelInfo
import com.github.freetube.core.extractor.channel.ChannelTab
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

    val tabResults = mutableStateOf(emptyList<ChannelTab>())

    init {
        screenModelScope.launch {
            channelRepository.getChannelData(url)
                .collect {
                    mutableState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> {
                            UiState.Error(it.message)
                        }

                        is Resource.Success -> {
                            tabResults.value = it.data.tabs
                            UiState.Success(it.data)
                        }
                    }
                }
        }
    }

    fun onAction(action: ChannelAction) {
        when (action) {
            is ChannelAction.GetTab -> getTab(getTabFromIndex(action.index), action.index)
            is ChannelAction.GetTabNextPage -> getTabNextPage(
                getTabFromIndex(action.index),
                action.index
            )
        }
    }

    private fun getTabFromIndex(index: Int): ChannelTab = tabResults.value[index]

//    private fun setTab(index: Int, new: ChannelTab) { tabResults.value[index] = new }

    private fun getTab(tab: ChannelTab, index: Int) {
        screenModelScope.launch {
            channelRepository.getTab(url, tab)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Error -> {
//                            setTab(index, getTab(index).copy(isLoading = false, error = it.message))
                        }

                        is Resource.Success -> {
                            getTabFromIndex(index).items + it.data
                        }
                    }
                }
        }
    }

    private fun getTabNextPage(tab: ChannelTab, index: Int) {
        screenModelScope.launch {
            channelRepository.getTabNextPage(url, tab)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Error -> {
//                            setTab(index, getTab(index).copy(isLoading = false, error = it.message))
                        }

                        is Resource.Success -> {
                            getTabFromIndex(index).items + it.data
                        }
                    }
                }
        }
    }

    override fun onDispose() {
        channelRepository.disposeChannel(url)
        super.onDispose()
    }
}
