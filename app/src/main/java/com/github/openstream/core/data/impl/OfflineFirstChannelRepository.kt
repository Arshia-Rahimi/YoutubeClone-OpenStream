package com.github.openstream.core.data.impl

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.extractor.ChannelExtractor
import com.github.openstream.core.model.extractordata.Channel
import com.github.openstream.core.model.extractordata.ChannelItem
import com.github.openstream.core.model.extractordata.ChannelTab
import com.github.openstream.core.model.extractordata.DataItem
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

    override fun getChannel(channelItem: ChannelItem): Flow<Resource<Channel>> =
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

    override fun getTab(
        channel: Channel,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            emit(ChannelExtractor.fetchTab(channel, tab))
        }.asResult(Dispatchers.IO)

    override fun getTabNextPage(
        channel: Channel,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            emit(ChannelExtractor.fetchNextPage(channel, tab))
        }.asResult(Dispatchers.IO)
    
    override suspend fun subscribe(channel: ChannelItem): Flow<Resource<Success>> =
        flow {
            // todo save videos and playlists
            if (db.channelDao().get(channel.url) == null) {
                db.channelDao().upsert(channel.toEntity())
            }
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun unSubscribe(channelId: Long): Flow<Resource<Success>> =
        flow {
            db.channelDao().delete(channelId)
            emit(Success)
        }.asResult(Dispatchers.IO)
}
