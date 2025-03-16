package com.github.freetube.core.data.imp

import android.os.Build
import androidx.annotation.RequiresApi
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.YtRepository
import com.github.freetube.core.extractor.DataItem
import com.github.freetube.core.extractor.SearchUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class NeYtRepository: YtRepository {
    
    private lateinit var search: SearchUnit
    
    @RequiresApi(Build.VERSION_CODES.S)
    override suspend fun search(
        query: String,
        contentFilter: List<String>?,
        sortFilter: String?,
    ): Flow<Resource<List<List<DataItem>>>> = withContext(Dispatchers.IO) {
        flow {
            search = SearchUnit(query, contentFilter, sortFilter)
            emit(search.result.toList())
        }.asResult()
    }
    
    override suspend fun getNextPage(): Flow<Resource<List<List<DataItem>>>> = withContext(Dispatchers.IO) {
        flow {
            search.fetchNextPage()
            emit(search.result)
        }.asResult()
    }
}
