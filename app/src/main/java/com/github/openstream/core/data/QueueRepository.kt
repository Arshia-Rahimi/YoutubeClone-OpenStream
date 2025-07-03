package com.github.openstream.core.data

import com.github.openstream.core.datastore.QueueModel
import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.flow.SharedFlow

interface QueueRepository {
    val queueModel: SharedFlow<QueueModel>
    
    fun replaceQueue(videos: List<VideoItem>, startIndex: Int)
    
    fun addToQueue(vararg video: VideoItem)
    
    fun playNext(newVideo: VideoItem)
    
    fun playFrom(index: Int)
    
    fun toggleShuffleMode()
    
    fun toggleRepeatModel()
}
