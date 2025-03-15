package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.youtubeService
import com.github.freetube.core.data.YtRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class NeYtRepository: YtRepository {
    override suspend fun search(
        query: String,
        contentFilter: List<String>?,
        sortFilter: String?,
    ): Flow<Resource<Class<*>>> = withContext(Dispatchers.IO) {
        flow {
            try {
                emit(Resource.Loading)
                val extractor = youtubeService.getSearchExtractor(query)
                extractor.fetchPage()
                val infoItemsPage = extractor.initialPage.nextPage
                emit(Resource.Success(infoItemsPage))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage))
            }
        }
    }
}
