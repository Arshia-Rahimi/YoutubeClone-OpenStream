package com.github.openstream.core.data.imp

import androidx.media3.common.MediaItem
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.extractor.VideoExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorVideoRepository : VideoRepository {
    override fun fetchVideo(url: String): Flow<Resource<MediaItem>> =
        flow {
            emit(VideoExtractor.fetchVideo(url))
        }.asResult(Dispatchers.IO)
}
