package com.github.freetube.core.datastore.proto.playerconfig

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map

class PlayerConfigDataStore(
    private val dataStore: DataStore<PlayerConfigDataStoreModel>,
) {
    val data = dataStore.data.map {
        PlayerConfigDataStoreModel(
            seekIncrement = it.seekIncrement,
        )
    }

    private suspend fun update(transform: PlayerConfigDataStoreModel.() -> PlayerConfigDataStoreModel) =
        dataStore.updateData(transform)

    suspend fun setSeekIncrement(seekIncrement: Long) =
        update { copy(seekIncrement = seekIncrement) }
}
