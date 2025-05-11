package com.github.openstream.core.data.impl

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.extractor.SearchExtractor
import com.github.openstream.core.model.extractordata.ChannelItem
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistItem
import com.github.openstream.core.model.extractordata.SearchResult
import com.github.openstream.core.model.extractordata.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorSearchRepository(
    private val db: OpenStreamDatabase,
) : SearchRepository {
    
    override suspend fun search(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): Flow<Resource<SearchResult>> = flow {
        val searchResult = SearchExtractor.fetchSearchResult(query, contentFilter, sortFilter)
        val firstPage = syncDataItemsWithDB(searchResult.items)
        emit(searchResult.copy(items = firstPage))
    }.asResult(Dispatchers.IO)
    
    override suspend fun getNextPage(currentSearch: SearchResult): Flow<Resource<List<DataItem>>> =
        flow {
            val result = SearchExtractor.fetchNextPage(currentSearch)
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
                    
                    is ChannelItem -> {
                        // todo sync when channels can be saved locally
                        add(item)
                    }
                    
                    else -> {}
                }
            }
        }
    
}
