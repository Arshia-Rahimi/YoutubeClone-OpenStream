package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.extractor.video.VideoData
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    suspend fun fetchVideo(url: String): Flow<Resource<VideoData>>
}
