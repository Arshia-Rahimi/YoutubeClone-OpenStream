package com.github.openstream.core.data.imp

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.SearchRepository
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.extractor.SearchExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorSearchRepository : SearchRepository {

    override suspend fun search(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): Flow<Resource<SearchExtractor>> = flow {
        emit(SearchExtractor(query, contentFilter, sortFilter))
    }.asResult(Dispatchers.IO)

    override suspend fun getNextPage(currentSearch: SearchExtractor): Flow<Resource<List<DataItem>?>> =
        flow { emit(currentSearch.fetchNextPage()) }
            .asResult(Dispatchers.IO)
}
