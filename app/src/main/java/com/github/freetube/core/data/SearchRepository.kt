package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.search.InitialSearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    
    suspend fun search(
        query: String,
        contentFilter: List<String> = emptyList(),
        sortFilter: String? = null,
    ): Flow<Resource<InitialSearchResult>>
    
    suspend fun getNextPage(): Flow<Resource<List<DataItem>?>>
}
