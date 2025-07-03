package com.github.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.openstream.core.data.PlayerConfigRepository
import com.github.openstream.core.datastore.PlayerConfigModel

class DataStorePlayerConfigRepository(
    private val dataStore: DataStore<PlayerConfigModel>,
) : PlayerConfigRepository {
    override val playerConfig = dataStore.data
    
    override suspend fun setPlaybackSpeed(speed: Float) {
        dataStore.updateData { it.copy(playbackSpeed = speed) }
    }

    override suspend fun setSeekIncrement(seekIncrement: Long) {
        dataStore.updateData { it.copy(seekIncrement = seekIncrement) }
    }
}
