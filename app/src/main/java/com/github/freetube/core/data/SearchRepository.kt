package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.search.SearchUnit
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    
    suspend fun search(
        query: String,
        contentFilter: List<String> = emptyList(),
        sortFilter: String? = null,
    ): Flow<Resource<SearchUnit>>

    suspend fun getNextPage(currentSearch: SearchUnit): Flow<Resource<List<DataItem>?>>
}
