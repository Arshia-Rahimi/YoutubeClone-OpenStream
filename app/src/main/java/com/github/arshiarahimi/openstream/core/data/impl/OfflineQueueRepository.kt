package com.github.arshiarahimi.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.arshiarahimi.openstream.core.data.QueueRepository
import com.github.arshiarahimi.openstream.core.datastore.QueueModel
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.flow.map

class OfflineQueueRepository(
    private val dataStore: DataStore<QueueModel>,
) : QueueRepository {
    override fun getQueue() = dataStore.data.map { it.queue }

    override suspend fun updateQueue(videos: List<VideoItem>) {
        dataStore.updateData { QueueModel(videos) }
    }
}
