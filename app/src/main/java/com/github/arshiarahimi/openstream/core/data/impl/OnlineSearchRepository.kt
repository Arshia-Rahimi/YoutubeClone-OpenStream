package com.github.arshiarahimi.openstream.core.data.impl

import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.asResult
import com.github.arshiarahimi.openstream.core.data.SearchRepository
import com.github.arshiarahimi.openstream.core.database.OpenStreamDatabase
import com.github.arshiarahimi.openstream.core.extractor.datasource.SearchRemoteDataSource
import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.model.extractordata.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OnlineSearchRepository(
    private val db: OpenStreamDatabase,
) : SearchRepository {

    override suspend fun search(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): Flow<Resource<SearchResult>> = flow {
        val searchResult =
            SearchRemoteDataSource.fetchSearchResult(query, contentFilter, sortFilter)
        val firstPage = syncDataItemsWithDB(searchResult.items)
        emit(searchResult.copy(items = firstPage))
    }.asResult(Dispatchers.IO)

    override suspend fun getNextPage(currentSearch: SearchResult): Flow<Resource<List<DataItem>>> =
        flow {
            val result = SearchRemoteDataSource.fetchNextPage(currentSearch)
            val nextPage = syncDataItemsWithDB(result)
            emit(nextPage)
        }.asResult(Dispatchers.IO)

    suspend fun syncDataItemsWithDB(dataItemList: List<DataItem>): List<DataItem> =
        buildList {
            dataItemList.map { item ->
                when (item) {
                    is PlaylistItem if item is PlaylistItem.OnlinePlaylistItem -> {
                        val playlistId = db.playlistDao().get(item.url)?.playlistId
                        if (playlistId == null) {
                            add(item)
                        } else {
                            val updatedPlaylist = item.toOfflineFirstPlaylistItem(playlistId)
                            db.playlistDao().upsert(updatedPlaylist.toEntity())
                            add(updatedPlaylist)
                        }
                    }

                    is VideoItem -> {
                        val videoId = db.videoDao().get(item.url)?.videoId

                        if (videoId == null) {
                            add(item)
                        } else {
                            val updatedVideo = item.copy(id = videoId)
                            db.videoDao().upsert(updatedVideo.toEntity())
                            add(updatedVideo)
                        }
                    }

                    is ChannelItem.OnlineChannelItem -> {
                        val channelId = db.channelDao().get(item.url)?.channelId
                        if (channelId == null) {
                            add(item)
                        } else {
                            val updatedChannel = item.toOfflineFirstChannelItem(channelId)
                            db.channelDao().upsert(updatedChannel.toEntity())
                            add(updatedChannel)
                        }
                    }

                    else -> {}
                }
            }
        }

}
