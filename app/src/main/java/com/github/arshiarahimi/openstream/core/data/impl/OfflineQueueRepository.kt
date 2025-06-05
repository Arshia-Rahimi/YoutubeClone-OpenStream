package com.github.arshiarahimi.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.arshiarahimi.openstream.core.data.QueueRepository
import com.github.arshiarahimi.openstream.core.datastore.QueueModel
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class OfflineQueueRepository(
    private val dataStore: DataStore<QueueModel>,
    private val scope: CoroutineScope,
) : QueueRepository {
    override fun getQueue() = dataStore.data.map { it.queue }
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

    override suspend fun updateQueue(videos: List<VideoItem>) {
        dataStore.updateData { QueueModel(videos) }
    }
}
