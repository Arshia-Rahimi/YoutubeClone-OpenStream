package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.SearchRepository
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.search.InitialSearchResult
import com.github.freetube.core.extractor.search.SearchUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ExtractorSearchRepository : SearchRepository {

    private var search: SearchUnit? = null
    
    override suspend fun search(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): Flow<Resource<InitialSearchResult>> = withContext(Dispatchers.IO) {
        flow {
            search = SearchUnit(query, contentFilter, sortFilter)
            emit(search?.getFirstPage() ?: InitialSearchResult("", false, emptyList()))
        }.asResult()
    }
    
    override suspend fun getNextPage(): Flow<Resource<List<DataItem>?>> = withContext(Dispatchers.IO) {
        flow { emit(search?.getNextPage()) }.asResult()
    }
}
