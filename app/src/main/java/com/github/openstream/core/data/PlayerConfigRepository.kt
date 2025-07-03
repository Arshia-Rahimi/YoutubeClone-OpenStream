package com.github.openstream.core.data

import com.github.openstream.core.datastore.PlayerConfigModel
import kotlinx.coroutines.flow.Flow

interface PlayerConfigRepository {
    val playerConfig: Flow<PlayerConfigModel>
    
    suspend fun setPlaybackSpeed(speed: Float)
    
    suspend fun setSeekIncrement(seekIncrement: Long)
}
