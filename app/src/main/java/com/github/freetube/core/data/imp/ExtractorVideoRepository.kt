package com.github.freetube.core.data.imp

import androidx.media3.common.MediaItem
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.VideoRepository
import com.github.freetube.core.extractor.video.VideoUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorVideoRepository : VideoRepository {
    override suspend fun fetchVideo(url: String): Flow<Resource<MediaItem>> =
        flow {
            val video = VideoUnit(url)
            emit(video.item)
        }.asResult(Dispatchers.IO)
}
