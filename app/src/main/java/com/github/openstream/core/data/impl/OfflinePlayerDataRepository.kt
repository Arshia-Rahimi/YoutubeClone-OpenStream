package com.github.openstream.core.data.impl

import androidx.datastore.core.DataStore
import com.github.openstream.core.common.util.next
import com.github.openstream.core.data.PlayerDataRepository
import com.github.openstream.core.datastore.PlayerDataModel
import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class OfflinePlayerDataRepository(
    private val dataStore: DataStore<PlayerDataModel>,
    scope: CoroutineScope,
) : PlayerDataRepository {
    
    override val playerData = dataStore.data
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), PlayerDataModel())
    
    override suspend fun setPlaybackSpeed(speed: Float) {
        dataStore.updateData { it.copy(playbackSpeed = speed) }
    }

    override suspend fun setSeekIncrement(seekIncrement: Long) {
        dataStore.updateData { it.copy(seekIncrement = seekIncrement) }
    }

    override suspend fun clearQueue() {
        dataStore.updateData { 
            playerData.value.copy(
                currentVideoIndex = null,
                queue = emptyList(),
            )
        }
    }

    override suspend fun replaceQueue(videos: List<VideoItem>, startIndex: Int) {
        dataStore.updateData { 
            playerData.value.copy(
                currentVideoIndex = startIndex,
                queue = videos,
            )
        }
    }

    override suspend fun addToQueue(vararg video: VideoItem) {
        dataStore.updateData {
            playerData.value.copy(
                queue = playerData.value.queue + video,
            )
        }
    }

    override suspend fun playNext(newVideo: VideoItem) {
        dataStore.updateData {
            val newQueue = playerData.value.queue.toMutableList()
            newQueue.add(playerData.value.currentVideoIndex?.plus(1) ?: 0, newVideo)
            playerData.value.copy(queue = newQueue)
        }
    }

    override suspend fun playFrom(index: Int) {
        dataStore.updateData { playerData.value.copy(currentVideoIndex = index) }
    }

    override suspend fun toggleShuffleMode() {
        dataStore.updateData {
            playerData.value.copy(isShuffleEnabled = !playerData.value.isShuffleEnabled)
        }
    }

    override suspend fun changeRepeatMode() {
        dataStore.updateData {
            playerData.value.copy(repeatMode = playerData.value.repeatMode.next())
        }
    }
}
