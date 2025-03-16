package com.github.freetube.core.data.imp

import android.os.Build
import androidx.annotation.RequiresApi
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.YtRepository
import com.github.freetube.core.extractor.DataItem
import com.github.freetube.core.extractor.InitialSearchResult
import com.github.freetube.core.extractor.SearchUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ExtractorYtRepository: YtRepository {
    
    private lateinit var search: SearchUnit
    
    @RequiresApi(Build.VERSION_CODES.S)
    override suspend fun search(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): Flow<Resource<InitialSearchResult>> = withContext(Dispatchers.IO) {
        flow {
            search = SearchUnit(query, contentFilter, sortFilter)
            emit(search.getFirstPage())
        }.asResult()
    }
    
    override suspend fun getNextPage(): Flow<Resource<List<DataItem>?>> = withContext(Dispatchers.IO) {
        flow { emit(search.getNextPage()) }.asResult()
    }
}
