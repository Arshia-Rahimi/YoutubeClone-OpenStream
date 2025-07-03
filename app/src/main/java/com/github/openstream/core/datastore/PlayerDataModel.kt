package com.github.openstream.core.datastore

import com.github.openstream.core.media3.PlayerRepeatMode
import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDataModel(
    val seekIncrement: Long = 10000L,
    val playbackSpeed: Float = 1F,
    val currentVideoIndex: Int? = null, 
    val queue: List<VideoItem> = emptyList(),
    val isShuffleEnabled: Boolean = false,
    val repeatMode: PlayerRepeatMode = PlayerRepeatMode.ALL,
)
