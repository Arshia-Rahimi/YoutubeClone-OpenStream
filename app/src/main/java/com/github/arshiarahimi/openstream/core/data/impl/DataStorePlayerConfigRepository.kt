package com.github.arshiarahimi.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.arshiarahimi.openstream.core.data.PlayerConfigRepository
import com.github.arshiarahimi.openstream.core.datastore.PlayerConfigModel

class DataStorePlayerConfigRepository(
    private val dataStore: DataStore<PlayerConfigModel>,
) : PlayerConfigRepository {
    override val playerConfig = dataStore.data

    override suspend fun updatePlayerConfig(config: PlayerConfigModel) {
        dataStore.updateData { config }
    }
}
