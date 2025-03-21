package com.github.freetube.ui.sharedscreens.channel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.data.ChannelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChannelScreenModel(
    private val url: String,
    private val channelRepository: ChannelRepository,
) : ScreenModel {

    private val data = MutableStateFlow(null)

    init {
        screenModelScope.launch {
            channelRepository.getChannelData(url)
                .collect {}
        }
    }

    override fun onDispose() {
        channelRepository.disposeChannel(url)
        super.onDispose()
    }
}
