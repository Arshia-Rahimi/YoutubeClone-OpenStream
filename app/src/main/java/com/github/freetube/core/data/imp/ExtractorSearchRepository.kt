package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.SearchRepository
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.search.SearchUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorSearchRepository : SearchRepository {

    override suspend fun search(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): Flow<Resource<SearchUnit>> = flow {
        emit(SearchUnit(query, contentFilter, sortFilter))
    }.asResult(Dispatchers.IO)

    override suspend fun getNextPage(currentSearch: SearchUnit): Flow<Resource<List<DataItem>?>> =
        flow { emit(currentSearch.fetchNextPage()) }
            .asResult(Dispatchers.IO)
}
