package com.github.arshiarahimi.openstream.core.data

import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.flow.SharedFlow

interface QueueRepository {
    fun getQueue(): SharedFlow<List<VideoItem>>

    suspend fun updateQueue(videos: List<VideoItem>)
}
