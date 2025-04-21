package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.Flow

interface SubscriptionsRepository {
    val channels: Flow<List<DataItem>>

    suspend fun toggleSubscribe(channel: DataItem.Channel): Flow<Resource<Success>>
}
