package com.github.openstream.core.data

import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.flow.SharedFlow

interface QueueRepository {
    val queue: SharedFlow<List<VideoItem>>
    
    val currentVideo: SharedFlow<VideoItem?>
    
    suspend fun replaceQueue(videos: List<VideoItem>)
    
    suspend fun addToQueue(vararg video: VideoItem)
    
    suspend fun playNext(newVideo: VideoItem)
    
    suspend fun playFrom(index: Int)
}
