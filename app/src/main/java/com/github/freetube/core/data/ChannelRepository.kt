package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.extractor.channel.ChannelInfo
import com.github.freetube.core.extractor.channel.ChannelTab
import com.github.freetube.core.extractor.channel.ChannelUnit
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {

    val channels: List<ChannelUnit>

    suspend fun getChannelData(channelUrl: String): Flow<Resource<ChannelInfo>>

    suspend fun getTab(channelUrl: String, tab: ChannelTab): Flow<Resource<Unit>>

    fun disposeChannel(channelUrl: String)
}
