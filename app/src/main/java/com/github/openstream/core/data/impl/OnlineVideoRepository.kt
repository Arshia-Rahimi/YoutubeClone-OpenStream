package com.github.openstream.core.data.impl

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.VideoRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.extractor.datasource.VideoRemoteDataSource
import com.github.openstream.core.shared.extractor.data.VideoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope

class OnlineVideoRepository(
    private val db: OpenStreamDatabase,
) : VideoRepository {
    override fun fetchVideo(url: String): Flow<Resource<VideoData>> = flow {
        val video = VideoRemoteDataSource.fetchVideo(url)

        // update video data in db if it's found
        db.videoDao().get(url)?.videoId?.let { id ->
            val videoData = video.copy(id = id)
            db.videoDao().upsert(videoData.toDataItem().toEntity())
        }
        
        emit(video)
    }.asResult(Dispatchers.IO, this::class.simpleName, "fetchVideo()")

    override fun deleteLocalVideoHistory(): Flow<Resource<Success>> =
        flow {
            supervisorScope {
                val d1 = async { db.videoDao().deleteAll() }
                val d2 = async { db.playlistDao().deleteAllVideos() }
                val d3 = async { db.channelDao().deleteAllVideos() }
                d1.await()
                d2.await()
                d3.await()
                emit(Success)
            }
        }.asResult(Dispatchers.IO, this::class.simpleName, "deleteLocalVideoHistory()")
}
