package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.YtDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.schabi.newpipe.extractor.ServiceList

class NeYtDataRepository: YtDataRepository {
    
    private val service = ServiceList.YouTube
    
    override suspend fun search(
        query: String,
        contentFilter: List<String>?,
        sortFilter: String?,
    ): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading)
            val extractor = service.getSearchExtractor(query, contentFilter, sortFilter)
            extractor.fetchPage()
            val infoItemsPage = extractor.initialPage
            println(infoItemsPage.toString())
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }
}
