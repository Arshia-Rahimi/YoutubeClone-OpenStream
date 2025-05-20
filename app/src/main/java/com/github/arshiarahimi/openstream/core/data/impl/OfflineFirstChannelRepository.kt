package com.github.arshiarahimi.openstream.core.data.impl

import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.Success
import com.github.arshiarahimi.openstream.core.common.util.asResult
import com.github.arshiarahimi.openstream.core.data.ChannelRepository
import com.github.arshiarahimi.openstream.core.database.OpenStreamDatabase
import com.github.arshiarahimi.openstream.core.extractor.datasource.ChannelRemoteDataSource
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.model.extractor.ChannelExtractor
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn


class OfflineFirstChannelRepository(
    private val db: OpenStreamDatabase,
    private val scope: CoroutineScope,
) : ChannelRepository {
    override val subscriptions = db.channelDao().index()
        .map { it.map { channel -> channel.toDataItem() } }
        .shareIn(
            scope = scope,
            started = SharingStarted.Lazily,
            replay = 1,
        )
    
    override fun subscribe(channel: ChannelItem): Flow<Resource<Success>> =
        flow {
            // todo save videos and playlists
            if (db.channelDao().get(channel.url) == null) {
                db.channelDao().upsert(channel.toEntity())
            }
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override fun unSubscribe(channelId: Long): Flow<Resource<Success>> =
        flow {
            db.channelDao().delete(channelId)
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override fun getChannel(channelItem: ChannelItem): Flow<Resource<ChannelExtractor>> =
        flow {
            when (channelItem) {
                is ChannelItem.OfflineFirstChannelItem -> {
                    val channelData =
                        db.channelDao().getChannelWithVideos(channelItem.id)
                            ?: throw Exception("channel was not found")
                    emit(channelData.toObject())
                }
                
                is ChannelItem.OnlineChannelItem -> {
                    // todo
                }
            }
        }.asResult(Dispatchers.IO)
    
    override fun getTabFirstPage(
        channel: ChannelExtractor,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            emit(ChannelRemoteDataSource.fetchTab(channel, tab))
        }.asResult(Dispatchers.IO)
    
    override fun getTabNextPage(
        channel: ChannelExtractor,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            emit(ChannelRemoteDataSource.fetchNextPage(channel, tab))
        }.asResult(Dispatchers.IO)
    
    override fun getChannelSavedVideos(channel: ChannelItem): Flow<List<VideoItem>?> {
        TODO("Not yet implemented")
    }
    
    override fun getChannelSavedPlaylists(channel: ChannelItem): Flow<List<PlaylistItem>?> {
        TODO("Not yet implemented")
    }
    
}
