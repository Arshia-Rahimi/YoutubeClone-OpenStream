package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.ChannelRepository
import com.github.freetube.core.extractor.channel.ChannelData
import com.github.freetube.core.extractor.channel.ChannelTab
import com.github.freetube.core.extractor.channel.ChannelUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorChannelRepository : ChannelRepository {
    override val channels = mutableListOf<ChannelUnit>()

    private fun getCurrentChannel(url: String): ChannelUnit? = channels.first { it.url == url }

    override suspend fun getChannelData(channelUrl: String): Flow<Resource<ChannelData>> =
        flow {
            val newChannel = ChannelUnit(channelUrl)
            channels += newChannel
            emit(newChannel.data)
        }.asResult(Dispatchers.IO)

    override suspend fun getTab(channelUrl: String, tab: ChannelTab): Flow<Resource<Unit>> =
        flow<Unit> {
            val currentChannel = getCurrentChannel(channelUrl)
            currentChannel
        }.asResult(Dispatchers.IO)

    override fun disposeChannel(channelUrl: String) {
        channels.removeAll { it.url == channelUrl }
    }
}
