package com.github.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.openstream.core.data.QueueRepository
import com.github.openstream.core.datastore.QueueModel
import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class OfflineQueueRepository(
    private val dataStore: DataStore<QueueModel>,
    scope: CoroutineScope,
) : QueueRepository {
    private val queueModel = dataStore.data
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)
    
    override val queue = queueModel.map { it.queue }
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)
    
    override val currentVideo =
        queue.map { queue -> queueModel.first().currentVideoIndex?.let { queue[it] } }
            .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)
    
    override suspend fun replaceQueue(videos: List<VideoItem>) {
        dataStore.updateData { QueueModel() }
    }
    
    override suspend fun addToQueue(vararg video: VideoItem) {
        dataStore.updateData {
            val currentQueueModel = queueModel.first()
            val newQueue = currentQueueModel.queue.toMutableList().apply { addAll(video) }
            currentQueueModel.copy(queue = newQueue)
        }
    }
    
    override suspend fun playNext(newVideo: VideoItem) {
        dataStore.updateData {
            val currentQueueModel = queueModel.first()
            
            val newQueue = currentQueueModel.queue.toMutableList()
                .apply {
                    val newVideoIndex = currentQueueModel.currentVideoIndex?.let { it + 1 } ?: 0
                    add(newVideoIndex, newVideo)
                }
            
            currentQueueModel.copy(queue = newQueue)
        }
    }
    
    override suspend fun playFrom(index: Int) {
        dataStore.updateData {
            queueModel.first().copy(index)
        }
    }
}
