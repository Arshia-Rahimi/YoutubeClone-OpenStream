package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.extractor.SearchExtractor
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    
    suspend fun search(
        query: String,
        contentFilter: List<String> = emptyList(),
        sortFilter: String? = null,
    ): Flow<Resource<SearchExtractor>>

    suspend fun getNextPage(currentSearch: SearchExtractor): Flow<Resource<List<DataItem>?>>
}
