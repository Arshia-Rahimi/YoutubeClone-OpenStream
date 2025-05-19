package com.github.arshiarahimi.openstream.core.data.impl

import androidx.media3.common.MediaItem
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.asResult
import com.github.arshiarahimi.openstream.core.data.VideoRepository
import com.github.arshiarahimi.openstream.core.database.OpenStreamDatabase
import com.github.arshiarahimi.openstream.core.extractor.datasource.VideoRemoteDataSource
import com.github.arshiarahimi.openstream.core.model.extractordata.VideoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorVideoRepository(
    private val db: OpenStreamDatabase,
) : VideoRepository {
    override fun fetchVideo(url: String): Flow<Resource<MediaItem>> =
        flow {
            val video = VideoRemoteDataSource.fetchVideo(url)
            val videoId = db.videoDao().get(url)?.videoId
            if (videoId != null) {
                video.localConfiguration?.tag =
                    (video.localConfiguration?.tag as VideoData).copy(id = videoId)
                db.videoDao()
                    .upsert((video.localConfiguration?.tag as VideoData).toDataItem().toEntity())
            }
            emit(video)
        }.asResult(Dispatchers.IO)
}
