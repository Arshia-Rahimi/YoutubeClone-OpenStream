package com.github.openstream.core.data.impl

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.extractor.datasource.SearchRemoteDataSource
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OnlineSearchRepository(
    private val db: OpenStreamDatabase,
) : SearchRepository {

    override fun search(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): Flow<Resource<SearchResult>> = flow {
        val searchResult =
            SearchRemoteDataSource.fetchSearchResult(query, contentFilter, sortFilter)
        val firstPage = syncDataItemsWithDB(searchResult.items)
        emit(searchResult.copy(items = firstPage))
    }.asResult(
        Dispatchers.IO, this::class.simpleName, "search()")

    override fun getNextPage(currentSearch: SearchResult): Flow<Resource<List<DataItem>>> =
        flow {
            val result = SearchRemoteDataSource.fetchNextPage(currentSearch)
            val nextPage = syncDataItemsWithDB(result)
            emit(nextPage)
        }.asResult(Dispatchers.IO, this::class.simpleName, "getNextPage()")

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
