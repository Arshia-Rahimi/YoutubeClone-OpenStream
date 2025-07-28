package com.github.openstream.core.data.impl

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.database.entities.crossrefs.ChannelVideoCrossRef
import com.github.openstream.core.extractor.datasource.ChannelRemoteDataSource
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.ChannelExtractor
import com.github.openstream.core.shared.extractor.data.ChannelTab
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
        .map { it.map { channel -> channel.toDataItem() }.sortedBy { channel -> channel.name } }
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
        }.asResult(Dispatchers.IO, this::class.simpleName, "subscribe()")

    override fun unSubscribe(channelId: Long): Flow<Resource<Success>> =
        flow {
            supervisorScope {
                val d1 = async { db.channelDao().delete(channelId) }
                val d2 = async { db.channelDao().deleteAllChannelVideos(channelId) }
                d1.await()
                d2.await()
                emit(Success)
            }
        }.asResult(Dispatchers.IO, this::class.simpleName, "unsubscribe()")

    override fun getChannel(url: String): Flow<Resource<ChannelExtractor>> = flow {
        emit(ChannelRemoteDataSource.getChannelData(url))
    }.asResult(Dispatchers.IO, this::class.simpleName, "getChannel()")

    override fun getTabFirstPage(
        channel: ChannelItem,
        tab: ChannelTab,
        channelExtractor: ChannelExtractor,
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
        }.asResult(Dispatchers.IO, this::class.simpleName, "getTabFirstPage()")

    override fun getTabNextPage(
        channel: ChannelItem,
        tab: ChannelTab,
        channelExtractor: ChannelExtractor,
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
        }.asResult(Dispatchers.IO, this::class.simpleName, "getTabNextPage()")

    override fun updateSubscriptions(): Flow<Resource<Success>> =
        flow {
            supervisorScope {
                val subscriptions = subscriptions.first()
                subscriptions.map { channel ->
                    async {
                        val extractor = ChannelRemoteDataSource.getChannelData(channel.url)
                        val tabs = extractor.tabs.filter {
                            it.name in listOf("videos", "livestreams")
                        }

                        val videos = buildList {
                            tabs.forEach { tab ->
                                ChannelRemoteDataSource.fetchTab(extractor, tab)
                                    ?.filterIsInstance<VideoItem>()
                                    ?.map { videoItem -> videoItem.toEntity() }
                                    ?.let { videos -> addAll(videos) }
                            }
                        }
                        val ids = db.videoDao().upsertAndReturnIds(*videos.toTypedArray())
                        db.channelDao().upsertChannelVideos(
                            *ids.map { ChannelVideoCrossRef(channel.id, it) }.toTypedArray()
                        )
                    }
                }.awaitAll()
                emit(Success)
            }
        }.asResult(Dispatchers.IO, this::class.simpleName, "updateSubscriptions()")

    override fun getChannelId(url: String) = db.channelDao().getChannelId(url)

}
