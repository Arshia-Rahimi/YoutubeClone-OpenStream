package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.extractor.DataItem
import kotlinx.coroutines.flow.Flow

interface YtRepository {
    
    suspend fun search(
        query: String,
        contentFilter: List<String>? = null,
        sortFilter: String? = null,
    ): Flow<Resource<List<List<DataItem>>>>
    
    suspend fun getNextPage(): Flow<Resource<List<List<DataItem>>>>
}
