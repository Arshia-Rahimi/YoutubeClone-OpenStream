package com.github.openstream.core.data

import com.github.openstream.core.datastore.PlayerDataModel
import kotlinx.coroutines.flow.StateFlow

interface PlayerDataRepository {
    val playerData: StateFlow<PlayerDataModel>

    suspend fun setPlaybackSpeed(speed: Float)

    suspend fun setSeekIncrement(seekIncrement: Long)
}
