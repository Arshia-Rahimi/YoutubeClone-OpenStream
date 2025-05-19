package com.github.arshiarahimi.openstream.core.data

import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.extractordata.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun search(
        query: String,
        contentFilter: List<String> = emptyList(),
        sortFilter: String? = null,
    ): Flow<Resource<SearchResult>>

    suspend fun getNextPage(currentSearch: SearchResult): Flow<Resource<List<DataItem>>>
}
