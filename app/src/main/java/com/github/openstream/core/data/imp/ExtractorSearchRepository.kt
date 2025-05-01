package com.github.openstream.core.data.imp

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.extractor.SearchExtractor
import com.github.openstream.core.model.extractordata.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorSearchRepository : SearchRepository {

    override fun search(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): Flow<Resource<SearchResult>> = flow {
        emit(SearchExtractor.fetchSearchResult(query, contentFilter, sortFilter))
    }.asResult(Dispatchers.IO)

    override fun getNextPage(currentSearch: SearchResult): Flow<Resource<Success>> =
        flow {
            SearchExtractor.fetchNextPage(currentSearch)
            emit(Success)
        }.asResult(Dispatchers.IO)
}
