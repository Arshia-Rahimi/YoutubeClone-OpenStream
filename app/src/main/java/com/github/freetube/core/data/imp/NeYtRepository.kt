package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.YtRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.schabi.newpipe.extractor.ServiceList

class NeYtRepository: YtRepository {
    
    private val service = ServiceList.YouTube
    
    override suspend fun search(
        query: String,
        contentFilter: List<String>?,
        sortFilter: String?,
    ): Flow<Resource<String>> = withContext(Dispatchers.IO) {
        flow {
            try {
                emit(Resource.Loading)
                val extractor = service.getSearchExtractor(query)
                extractor.fetchPage()
                val infoItemsPage = extractor.initialPage
                emit(Resource.Success(infoItemsPage.toString()))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage))
            }
        }
    }
}
