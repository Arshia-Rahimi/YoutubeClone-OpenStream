package com.github.arshiarahimi.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.arshiarahimi.openstream.core.data.PlayerConfigRepository
import com.github.arshiarahimi.openstream.core.datastore.PlayerConfigModel
import com.github.arshiarahimi.openstream.core.media3.PlayerRepeatMode

class DataStorePlayerConfigRepository(
    private val dataStore: DataStore<PlayerConfigModel>,
) : PlayerConfigRepository {
    override val playerConfig = dataStore.data
    
    override suspend fun setShuffleMode(isShuffleEnabled: Boolean) {
        dataStore.updateData { it.copy(isShuffleEnabled = isShuffleEnabled) }
    }
    
    override suspend fun setRepeatMode(repeatMode: PlayerRepeatMode) {
        dataStore.updateData { it.copy(playerRepeatMode = repeatMode) }
    }
    
    override suspend fun setPlaybackSpeed(speed: Float) {
        dataStore.updateData { it.copy(playbackSpeed = speed) }
    }
}
