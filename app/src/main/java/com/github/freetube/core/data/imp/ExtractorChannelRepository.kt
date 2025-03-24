package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.ChannelRepository
import com.github.freetube.core.extractor.channel.ChannelTab
import com.github.freetube.core.extractor.channel.ChannelUnit
import com.github.freetube.core.extractor.model.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorChannelRepository : ChannelRepository {

    override suspend fun getChannelData(channelUrl: String): Flow<Resource<ChannelUnit>> =
        flow { emit(ChannelUnit(channelUrl)) }.asResult(Dispatchers.IO)

    override suspend fun getTab(
        channelUrl: String,
        tab: ChannelTab,
        currentChannel: ChannelUnit,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            emit(currentChannel.fetchTab(tab))
        }.asResult(Dispatchers.IO)

    override suspend fun getTabNextPage(
        channelUrl: String,
        tab: ChannelTab,
        currentChannel: ChannelUnit,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            emit(currentChannel.fetchNextPage(tab))
        }.asResult(Dispatchers.IO)
}
