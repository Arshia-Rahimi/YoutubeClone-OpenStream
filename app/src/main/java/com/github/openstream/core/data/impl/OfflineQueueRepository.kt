package com.github.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.openstream.core.common.util.next
import com.github.openstream.core.data.QueueRepository
import com.github.openstream.core.datastore.QueueModel
import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class OfflineQueueRepository(
    private val dataStore: DataStore<QueueModel>,
    private val scope: CoroutineScope,
) : QueueRepository {
    
    // todo implement shuffle
    override val queueModel = dataStore.data
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)
    
    override fun replaceQueue(videos: List<VideoItem>, startIndex: Int) {
        scope.launch { dataStore.updateData { QueueModel(startIndex, videos) } }
    }
    
    override fun addToQueue(vararg video: VideoItem) {
        scope.launch {
            dataStore.updateData {
                val currentQueueModel = queueModel.first()
                currentQueueModel.copy(queue = currentQueueModel.queue + video)
            }
        }
    }
    
    override fun playNext(newVideo: VideoItem) {
        scope.launch {
            dataStore.updateData {
                val currentQueueModel = queueModel.first()
                val newQueue = currentQueueModel.queue.toMutableList()
                newQueue.add(currentQueueModel.currentVideoIndex?.plus(1) ?: 0, newVideo)
                currentQueueModel.copy(queue = newQueue)
            }
        }
    }
    
    override fun playFrom(index: Int) {
        scope.launch {
            dataStore.updateData {
                queueModel.first().copy(index)
            }
        }
    }

    override fun toggleShuffleMode() {
        scope.launch { 
            dataStore.updateData { 
                val currentQueueModel = queueModel.first()
                currentQueueModel.copy(isShuffleEnabled = !currentQueueModel.isShuffleEnabled)
            }
        }
    }

    override fun toggleRepeatModel() {
        scope.launch { 
            dataStore.updateData { 
                val currentQueueModel = queueModel.first()
                currentQueueModel.copy(repeatMode = currentQueueModel.repeatMode.next())
            }
        }
    }
}
