package com.github.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.openstream.core.common.util.next
import com.github.openstream.core.data.PlayerDataRepository
import com.github.openstream.core.datastore.PlayerDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class OfflinePlayerDataRepository(
    private val dataStore: DataStore<PlayerDataModel>,
    scope: CoroutineScope,
) : PlayerDataRepository {
    
    override val playerData = dataStore.data
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), PlayerDataModel())

    override suspend fun changeRepeatMode() {
        dataStore.updateData {
            playerData.value.copy(repeatMode = playerData.value.repeatMode.next())
        }
    }

    override suspend fun setPlaybackSpeed(speed: Float) {
        dataStore.updateData { it.copy(playbackSpeed = speed) }
    }

    override suspend fun setSeekIncrement(seekIncrement: Long) {
        dataStore.updateData { it.copy(seekIncrement = seekIncrement) }
    }

}
