package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.extractor.channel.ChannelTab
import com.github.freetube.core.extractor.channel.ChannelUnit
import com.github.freetube.core.extractor.model.DataItem
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    suspend fun getChannelData(channelUrl: String): Flow<Resource<ChannelUnit>>

    suspend fun getTab(
        channelUrl: String,
        tab: ChannelTab,
        currentChannel: ChannelUnit
    ): Flow<Resource<List<DataItem>?>>

    suspend fun getTabNextPage(
        channelUrl: String,
        tab: ChannelTab,
        currentChannel: ChannelUnit
    ): Flow<Resource<List<DataItem>?>>
}
