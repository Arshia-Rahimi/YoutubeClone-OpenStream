package com.github.openstream.core.data.imp

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.extractor.SearchExtractor
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorSearchRepository(
    private val db: OpenStreamDatabase,
) : SearchRepository {
    
    override fun search(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): Flow<Resource<SearchResult>> = flow {
        val searchResult = SearchExtractor.fetchSearchResult(query, contentFilter, sortFilter)
        val firstPage = mutableListOf<DataItem>()
        coroutineScope {
            searchResult.items.map { item ->
                async {
                    when (item) {
                        is DataItem.Playlist -> {
                            // todo
                        }
                        
                        is DataItem.Video -> {
                            val videoId = db.videoDao().get(item.url)?.videoId
                            
                            if (videoId == null) {
                                firstPage += item
                                return@async
                            }
                            
                            val updatedVideo = item.copy(id = videoId)
                            db.videoDao().upsert(updatedVideo.toEntity())
                            firstPage += updatedVideo
                        }
                        
                        is DataItem.Channel -> {
                            // todo sync
                            firstPage += item
                        }
                    }
                }
            }.awaitAll()
        }
        emit(searchResult.copy(items = firstPage))
    }.asResult(Dispatchers.IO)
    
    override fun getNextPage(currentSearch: SearchResult): Flow<Resource<List<DataItem>>> =
        flow {
            val result = SearchExtractor.fetchNextPage(currentSearch)
            val nextPage = mutableListOf<DataItem>()
            
            coroutineScope {
                result.map { item ->
                    async {
                        when (item) {
                            is DataItem.Playlist -> {
                                // todo
                            }
                            
                            is DataItem.Video -> {
                                val videoId = db.videoDao().get(item.url)?.videoId
                                
                                if (videoId == null) {
                                    nextPage += item
                                    return@async
                                }
                                
                                val updatedVideo = item.copy(id = videoId)
                                db.videoDao().upsert(updatedVideo.toEntity())
                                nextPage += updatedVideo
                            }
                            
                            is DataItem.Channel -> {
                                // todo sync
                                nextPage += item
                            }
                        }
                    }
                }.awaitAll()
            }
            emit(nextPage)
        }.asResult(Dispatchers.IO)
    
}
