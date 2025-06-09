package com.github.openstream.core.data

import com.github.openstream.core.datastore.PlayerConfigModel
import com.github.openstream.core.media3.PlayerRepeatMode
import kotlinx.coroutines.flow.Flow

interface PlayerConfigRepository {
    val playerConfig: Flow<PlayerConfigModel>
    
    suspend fun setShuffleMode(isShuffleEnabled: Boolean)
    
    suspend fun setRepeatMode(repeatMode: PlayerRepeatMode)
    
    suspend fun setPlaybackSpeed(speed: Float)
}
