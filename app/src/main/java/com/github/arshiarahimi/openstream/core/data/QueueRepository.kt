package com.github.arshiarahimi.openstream.core.data

import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.Success
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.flow.Flow

interface QueueRepository {
    val queue: Flow<List<VideoItem>>

    fun addToQueueAfterVideo(newVideo: VideoItem, videoAfter: VideoItem): Flow<Resource<Success>>
   
    fun removeFromQueue(video: VideoItem)

    fun updateQueue(videos: List<VideoItem>)
}
