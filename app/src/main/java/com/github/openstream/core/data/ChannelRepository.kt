package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.extractordata.Channel
import com.github.openstream.core.model.extractordata.ChannelItem
import com.github.openstream.core.model.extractordata.ChannelTab
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.OnlineChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ChannelRepository {
    val subscriptions: SharedFlow<List<ChannelItem>>
    
    fun getChannel(channelId: Long): Flow<Resource<Channel>>
    
    fun getChannel(channelUrl: String): Flow<Resource<Channel>>

    fun getTab(
        channel: OnlineChannel,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>>

    fun getTabNextPage(
        channel: OnlineChannel,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>>
    
    suspend fun subscribe(channel: ChannelItem): Flow<Resource<Success>>
    
    suspend fun unSubscribe(channelId: Long): Flow<Resource<Success>>
}
