package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.extractor.SearchExtractor
import com.github.openstream.core.model.extractordata.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    
    suspend fun search(
        query: String,
        contentFilter: List<String> = emptyList(),
        sortFilter: String? = null,
    ): Flow<Resource<SearchResult>>

    suspend fun getNextPage(currentSearch: SearchResult): Flow<Resource<Success>>
}
