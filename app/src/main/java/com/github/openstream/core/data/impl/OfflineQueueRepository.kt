package com.github.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.openstream.core.data.QueueRepository
import com.github.openstream.core.datastore.QueueModel
import kotlinx.coroutines.CoroutineScope

class OfflineQueueRepository(
    private val dataStore: DataStore<QueueModel>,
    scope: CoroutineScope,
) : QueueRepository {

}
