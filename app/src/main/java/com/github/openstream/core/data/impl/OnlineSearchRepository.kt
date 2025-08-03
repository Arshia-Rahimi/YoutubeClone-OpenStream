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
        Dispatchers.IO, this::class.simpleName, "search()"
    )
    
    override fun getNextPage(currentSearch: SearchResult): Flow<Resource<List<DataItem>>> =
        flow {
            val result = SearchRemoteDataSource.fetchNextPage(currentSearch)
            val nextPage = syncDataItemsWithDB(result)
            emit(nextPage)
        }.asResult(Dispatchers.IO, this::class.simpleName, "getNextPage")
    
    private suspend fun syncDataItemsWithDB(dataItemList: List<DataItem>): List<DataItem> =
        buildList {
            dataItemList.map { item ->
                when (item) {
                    is PlaylistItem.OnlinePlaylistItem -> {
                        var playlist = item
                        db.playlistDao().get(playlist.url)?.playlistId?.let { id ->
                            playlist = playlist.toOfflineFirstPlaylistItem(id)
                        }
                        add(playlist)
                    }
                    
                    is VideoItem -> {
                        var video = item
                        db.videoDao().get(item.url)?.videoId?.let { id ->
                            video = video.copy(id = id)
                            db.videoDao().upsertAndReturnIds(video.toEntity())
                        }
                        add(video)
                    }
                    
                    is ChannelItem.OnlineChannelItem -> {
                        var channel = item
                        db.channelDao().get(item.url)?.channelId?.let { id ->
                            channel = channel.toOfflineFirstChannelItem(id)
                        }
                        add(channel)
                    }
                    
                    else -> add(item)
                }
            }
        }
    
}
