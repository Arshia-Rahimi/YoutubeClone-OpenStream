package com.github.openstream.core.data.imp

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.model.extractordata.ChannelTab
import com.github.openstream.core.model.extractordata.ChannelUnit
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorChannelRepository : ChannelRepository {

    override fun getChannelData(channelUrl: String): Flow<Resource<ChannelUnit>> =
        flow { emit(ChannelUnit(channelUrl)) }.asResult(Dispatchers.IO)

    override fun getTab(
        tab: ChannelTab,
        currentChannel: ChannelUnit,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            emit(currentChannel.fetchTab(tab))
        }.asResult(Dispatchers.IO)

    override fun getTabNextPage(
        tab: ChannelTab,
        currentChannel: ChannelUnit,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            emit(currentChannel.fetchNextPage(tab))
        }.asResult(Dispatchers.IO)
}
