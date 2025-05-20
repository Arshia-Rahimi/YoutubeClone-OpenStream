package com.github.arshiarahimi.openstream.core.data

import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.Success
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.model.extractor.ChannelExtractor
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTab
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ChannelRepository {
    val subscriptions: SharedFlow<List<ChannelItem>>
    
    // online
    fun subscribe(channel: ChannelItem): Flow<Resource<Success>>
    
    // offlineFirst
    fun unSubscribe(channelId: Long): Flow<Resource<Success>>
    
    fun getChannel(channelItem: ChannelItem): Flow<Resource<ChannelExtractor>>
    
    fun getTabFirstPage(
        channelExtractor: ChannelExtractor,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>>
    
    fun getTabNextPage(
        channelExtractor: ChannelExtractor,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>>
    
    fun getChannelSavedVideos(channel: ChannelItem): Flow<List<VideoItem>?>
    
    fun getChannelSavedPlaylists(channel: ChannelItem): Flow<List<PlaylistItem>?>
    
}
