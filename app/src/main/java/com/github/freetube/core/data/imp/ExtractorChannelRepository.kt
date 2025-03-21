package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.ChannelRepository
import com.github.freetube.core.extractor.channel.ChannelData
import com.github.freetube.core.extractor.channel.ChannelUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ExtractorChannelRepository : ChannelRepository {
    override val channels = mutableListOf<ChannelUnit>()

    override suspend fun getChannelData(channelUrl: String): Flow<Resource<ChannelData>> =
        withContext(Dispatchers.IO) {
            flow {
                val newChannel = ChannelUnit(channelUrl)
                channels += newChannel
                emit(newChannel.data)
            }.asResult()
        }

    private fun getCurrentChannel(url: String): ChannelUnit = channels.first { it.url == url }

    override suspend fun getHomeTab(channelUrl: String): Flow<Resource<Unit>> =
        withContext(Dispatchers.IO) {
            flow<Unit> {
                val currentChannel = getCurrentChannel(channelUrl)
                currentChannel
            }.asResult()
        }

    override suspend fun getVideosTab(channelUrl: String): Flow<Resource<Unit>> =
        withContext(Dispatchers.IO) {
            flow<Unit> {
                val currentChannel = getCurrentChannel(channelUrl)
                currentChannel
            }.asResult()
        }

    override suspend fun getPlaylistsTab(channelUrl: String): Flow<Resource<Unit>> =
        withContext(Dispatchers.IO) {
            flow<Unit> {
                val currentChannel = getCurrentChannel(channelUrl)
                currentChannel
            }.asResult()
        }

    override fun disposeChannel(channelUrl: String) {
        channels.removeAll { it.url == channelUrl }
    }
}
