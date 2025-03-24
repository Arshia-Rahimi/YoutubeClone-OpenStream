package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.VideoRepository
import com.github.freetube.core.extractor.video.VideoData
import com.github.freetube.core.extractor.video.VideoUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorVideoRepository : VideoRepository {
    override suspend fun fetchVideo(url: String): Flow<Resource<VideoData>> =
        flow {
            val video = VideoUnit(url)
            emit(video.data)
        }.asResult(Dispatchers.IO)
}
