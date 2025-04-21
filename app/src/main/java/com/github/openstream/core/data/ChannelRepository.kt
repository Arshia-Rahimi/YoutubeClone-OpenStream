package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.model.extractordata.ChannelTab
import com.github.openstream.core.model.extractordata.ChannelUnit
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    suspend fun getChannelData(channelUrl: String): Flow<Resource<ChannelUnit>>

    suspend fun getTab(
        tab: ChannelTab,
        currentChannel: ChannelUnit
    ): Flow<Resource<List<DataItem>?>>

    suspend fun getTabNextPage(
        tab: ChannelTab,
        currentChannel: ChannelUnit
    ): Flow<Resource<List<DataItem>?>>
}
