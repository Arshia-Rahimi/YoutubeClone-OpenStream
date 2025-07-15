package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.extractor.data.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun search(
        query: String,
        contentFilter: List<String> = emptyList(),
        sortFilter: String? = null,
    ): Flow<Resource<SearchResult>>

    fun getNextPage(currentSearch: SearchResult): Flow<Resource<List<DataItem>>>
}
