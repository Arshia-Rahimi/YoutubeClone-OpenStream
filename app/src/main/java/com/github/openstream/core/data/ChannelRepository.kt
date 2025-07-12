package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.dataitem.ChannelItem
import com.github.openstream.core.model.dataitem.DataItem
import com.github.openstream.core.model.dataitem.VideoItem
import com.github.openstream.core.model.extractor.ChannelExtractor
import com.github.openstream.core.model.extractordata.ChannelTab
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ChannelRepository {
    val subscriptions: SharedFlow<List<ChannelItem>>

    val subscribedVideos: Flow<List<VideoItem>>

    fun subscribe(channel: ChannelItem.OnlineChannelItem): Flow<Resource<ChannelItem.OfflineFirstChannelItem>>
    
    fun unSubscribe(channelId: Long): Flow<Resource<Success>>

    fun getChannel(url: String): Flow<Resource<ChannelExtractor>>
    
    fun getTabFirstPage(
        channel: ChannelItem,
        channelExtractor: ChannelExtractor,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>>
    
    fun getTabNextPage(
        channel: ChannelItem,
        channelExtractor: ChannelExtractor,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>>

    fun updateSubscriptions(): Flow<Resource<Success>>
    
    fun isChannelsubscribed(channelUrl: String): Flow<Boolean>
    
}
