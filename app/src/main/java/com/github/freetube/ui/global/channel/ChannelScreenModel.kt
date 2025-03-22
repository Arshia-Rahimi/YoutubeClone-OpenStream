package com.github.freetube.ui.global.channel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.ChannelRepository
import com.github.freetube.core.extractor.channel.ChannelInfo
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

    init {
        screenModelScope.launch {
            channelRepository.getChannelData(url)
                .collect {
                    mutableState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> UiState.Error(it.message)
                        is Resource.Success -> UiState.Success(it.data)
                    }
                }
        }
    }

    override fun onDispose() {
        channelRepository.disposeChannel(url)
        super.onDispose()
    }
}
