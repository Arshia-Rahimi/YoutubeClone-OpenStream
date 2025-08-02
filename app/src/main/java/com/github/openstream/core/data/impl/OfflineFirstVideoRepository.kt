package com.github.openstream.core.data.impl

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.extractor.datasource.VideoRemoteDataSource
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.VideoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OfflineFirstVideoRepository(
    private val db: OpenStreamDatabase,
) : VideoRepository {
    override fun fetchVideo(url: String): Flow<Resource<VideoData>> = flow {
        var video = VideoRemoteDataSource.fetchVideo(url)

        // update video data in db if it's found
        db.videoDao().get(url)?.let {
            video = video.copy(id = it.videoId, position = it.position)
            db.videoDao().upsert(video.toDataItem().toEntity())
        }
        
        emit(video)
    }.asResult(Dispatchers.IO, this::class.simpleName, "fetchVideo()")

    override suspend fun saveVideo(video: VideoItem) {
        db.videoDao().upsert(video.toEntity())
    }

}
