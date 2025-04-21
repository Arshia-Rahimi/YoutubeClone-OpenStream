package com.github.openstream.core.data.imp

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.SubscriptionsRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.database.entities.ChannelEntity
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RoomSubscriptionsRepository(
    private val db: OpenStreamDatabase,
) : SubscriptionsRepository {
    override val channels = db.channelDao().index().map {
        it.map {
            DataItem.Channel(
                name = it.name,
                url = it.url ?: "",
                thumbnail = it.avatar,
                subscriberCount = it.subscriberCount,
                verified = it.isVerified,
                description = it.description,
            )
        }
    }

    override suspend fun toggleSubscribe(channel: DataItem.Channel): Flow<Resource<Success>> =
        flow {
            val isSubscribed = db.channelDao().index().first().any { it.url == channel.url }
            val channel = channel.toEntity(null) as ChannelEntity

            if (isSubscribed) db.channelDao().delete(channel)
            else db.channelDao().upsert(channel)

            emit(Success)
        }.asResult(Dispatchers.IO)
}
