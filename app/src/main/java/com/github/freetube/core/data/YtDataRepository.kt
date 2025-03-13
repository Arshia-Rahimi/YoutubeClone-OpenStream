package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import kotlinx.coroutines.flow.Flow

interface YtDataRepository {
    
    suspend fun search(
        query: String,
        contentFilter: List<String>? = null,
        sortFilter: String? = null,
    ): Flow<Resource<Unit>>
}
