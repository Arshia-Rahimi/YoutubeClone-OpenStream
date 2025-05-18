package com.github.arshiarahimi.openstream.core.data

import com.github.arshiarahimi.openstream.core.datastore.PlayerConfigModel
import kotlinx.coroutines.flow.Flow

interface PlayerConfigRepository {
    val playerConfig: Flow<PlayerConfigModel>

    suspend fun updatePlayerConfig(config: PlayerConfigModel)
}
