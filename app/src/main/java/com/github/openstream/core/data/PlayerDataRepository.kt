package com.github.openstream.core.data

import com.github.openstream.core.datastore.PlayerDataModel
import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.flow.StateFlow

interface PlayerDataRepository {
    val playerData: StateFlow<PlayerDataModel>

    suspend fun clearQueue()

    suspend fun replaceQueue(videos: List<VideoItem>, startIndex: Int)

    suspend fun addToQueue(vararg video: VideoItem)

    suspend fun playNext(newVideo: VideoItem)

    suspend fun playFrom(index: Int)

    suspend fun toggleShuffleMode()

    suspend fun changeRepeatMode()

    suspend fun setPlaybackSpeed(speed: Float)

    suspend fun setSeekIncrement(seekIncrement: Long)
}
