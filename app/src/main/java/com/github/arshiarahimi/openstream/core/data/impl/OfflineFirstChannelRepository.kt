package com.github.arshiarahimi.openstream.core.data.impl

import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.Success
import com.github.arshiarahimi.openstream.core.common.util.asResult
import com.github.arshiarahimi.openstream.core.data.ChannelRepository
import com.github.arshiarahimi.openstream.core.database.OpenStreamDatabase
import com.github.arshiarahimi.openstream.core.database.entities.crossrefs.ChannelVideoCrossRef
import com.github.arshiarahimi.openstream.core.extractor.datasource.ChannelRemoteDataSource
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.model.extractor.ChannelExtractor
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.supervisorScope

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

    override val subscribedVideos = db.channelDao().getAllChannelVideos()
        .map { list -> list.map { video -> video.toDataItem() } }

    override fun subscribe(channel: ChannelItem.OnlineChannelItem): Flow<Resource<ChannelItem.OfflineFirstChannelItem>> =
        flow {
            val id = db.channelDao().insert(channel.toEntity())
            emit(channel.toOfflineFirstChannelItem(id))
        }.asResult(Dispatchers.IO)

    override fun unSubscribe(channelId: Long): Flow<Resource<Success>> =
        flow {
            supervisorScope {
                val d1 = async { db.channelDao().delete(channelId) }
                val d2 = async { db.channelDao().deleteAllChannelVideos(channelId) }
                d1.await()
                d2.await()
                emit(Success)
            }
        }.asResult(Dispatchers.IO)

    override fun getChannel(url: String): Flow<Resource<ChannelExtractor>> =
        flow<ChannelExtractor> {
            emit(ChannelRemoteDataSource.getChannelData(url))
        }.asResult(Dispatchers.IO)

    override fun getTabFirstPage(
        channel: ChannelItem,
        channelExtractor: ChannelExtractor,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            val nextPage = ChannelRemoteDataSource.fetchTab(channelExtractor, tab)
            if (tab.name in "videos" && channel is ChannelItem.OfflineFirstChannelItem) {
                val ids = db.videoDao()
                    .upsertAndReturnIds(
                        *nextPage?.filterIsInstance<VideoItem>()
                            ?.map { it.toEntity() }
                            ?.toTypedArray() ?: emptyArray()
                    )
                db.channelDao()
                    .upsertChannelVideos(
                        *ids
                            .map { ChannelVideoCrossRef(channel.id, it) }
                            .toTypedArray()
                    )
            }
            emit(nextPage)
        }.asResult(Dispatchers.IO)

    override fun getTabNextPage(
        channel: ChannelItem,
        channelExtractor: ChannelExtractor,
        tab: ChannelTab,
    ): Flow<Resource<List<DataItem>?>> =
        flow {
            val nextPage = ChannelRemoteDataSource.fetchNextPage(channelExtractor, tab)
            if (tab.name in "videos" && channel is ChannelItem.OfflineFirstChannelItem) {
                val ids = db.videoDao()
                    .upsertAndReturnIds(
                        *nextPage?.filterIsInstance<VideoItem>()
                            ?.map { it.toEntity() }
                            ?.toTypedArray() ?: emptyArray()
                    )
                db.channelDao()
                    .upsertChannelVideos(
                        *ids
                            .map { ChannelVideoCrossRef(channel.id, it) }
                            .toTypedArray()
                    )
            }
            emit(nextPage)
        }.asResult(Dispatchers.IO)

    override suspend fun updateSubscriptions(): Flow<Resource<Success>> =
        flow {
            supervisorScope {
                val subscriptions = subscriptions.first()
                subscriptions.map { channel ->
                    async {
                        val extractor = ChannelRemoteDataSource.getChannelData(channel.url)
                        val videosTab = extractor.tabs?.firstOrNull { it.name == "videos" }
                            ?: return@async
                        val nextPage = ChannelRemoteDataSource.fetchTab(extractor, videosTab)
                        val ids = db.videoDao()
                            .upsertAndReturnIds(
                                *nextPage?.filterIsInstance<VideoItem>()
                                    ?.map { it.toEntity() }
                                    ?.toTypedArray() ?: emptyArray()
                            )
                        db.channelDao()
                            .upsertChannelVideos(
                                *ids
                                    .map { ChannelVideoCrossRef(channel.id, it) }
                                    .toTypedArray()
                            )
                    }
                }.awaitAll()
                emit(Success)
            }
        }.asResult(Dispatchers.IO)

}
