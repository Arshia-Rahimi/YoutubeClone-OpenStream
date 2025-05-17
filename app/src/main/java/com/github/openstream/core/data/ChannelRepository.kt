package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.extractordata.Channel
import com.github.openstream.core.model.extractordata.ChannelItem
import com.github.openstream.core.model.extractordata.ChannelTab
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ChannelRepository {
    val subscriptions: SharedFlow<List<ChannelItem>>

    fun getChannel(channelItem: ChannelItem): Flow<Resource<Channel>>

    fun getTab(
        channel: Channel,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>>

    fun getTabNextPage(
        channel: Channel,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>>

    fun subscribe(channel: ChannelItem): Flow<Resource<Success>>

    fun unSubscribe(channelId: Long): Flow<Resource<Success>>
}
