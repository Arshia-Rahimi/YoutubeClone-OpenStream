package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.extractor.channel.ChannelData
import com.github.freetube.core.extractor.channel.ChannelUnit
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {

    val channels: List<ChannelUnit>

    suspend fun getChannelData(channelUrl: String): Flow<Resource<ChannelData>>

    suspend fun getHomeTab(channelUrl: String): Flow<Resource<Unit>>

    suspend fun getVideosTab(channelUrl: String): Flow<Resource<Unit>>

    suspend fun getPlaylistsTab(channelUrl: String): Flow<Resource<Unit>>

    fun disposeChannel(channelUrl: String)
}
