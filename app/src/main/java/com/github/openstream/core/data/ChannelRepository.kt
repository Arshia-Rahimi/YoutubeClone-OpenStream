package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.extractor.ChannelExtractor
import com.github.openstream.core.model.extractordata.ChannelItem
import com.github.openstream.core.model.extractordata.ChannelTab
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ChannelRepository {
    val subscriptions: SharedFlow<List<ChannelItem>>
    
    fun getChannelData(channelUrl: String): Flow<Resource<ChannelExtractor>>

    fun getTab(
        tab: ChannelTab,
        currentChannel: ChannelExtractor
    ): Flow<Resource<List<DataItem>?>>

    fun getTabNextPage(
        tab: ChannelTab,
        currentChannel: ChannelExtractor
    ): Flow<Resource<List<DataItem>?>>
    
    suspend fun subscribe(channel: ChannelItem): Flow<Resource<Success>>
    
    suspend fun unSubscribe(channelId: Long): Flow<Resource<Success>>
}
