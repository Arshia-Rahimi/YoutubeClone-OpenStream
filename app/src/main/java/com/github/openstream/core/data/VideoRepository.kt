package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.VideoData
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun fetchVideo(url: String): Flow<Resource<VideoData>>
    
    suspend fun saveVideo(video: VideoItem)
    
    fun isInPlaylist(videoId: Long, playlistId: Long): Flow<Boolean>
    
}
