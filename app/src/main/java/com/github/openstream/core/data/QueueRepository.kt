package com.github.openstream.core.data

import com.github.openstream.core.datastore.QueueModel
import kotlinx.coroutines.flow.StateFlow

interface QueueRepository {
    val queueModel: StateFlow<QueueModel>
    
}
