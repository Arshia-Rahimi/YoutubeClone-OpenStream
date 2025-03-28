package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.Success
import com.github.freetube.core.extractor.model.DataItem
import kotlinx.coroutines.flow.Flow

interface SubscriptionsRepository {
    val channels: Flow<List<DataItem>>

    suspend fun toggleSubscribe(channel: DataItem.Channel): Flow<Resource<Success>>
}
