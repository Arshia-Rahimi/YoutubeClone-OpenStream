package com.github.arshiarahimi.openstream.core.data

import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.flow.Flow

interface QueueRepository {
    fun getQueue(): Flow<List<VideoItem>>

    suspend fun updateQueue(videos: List<VideoItem>)
}
