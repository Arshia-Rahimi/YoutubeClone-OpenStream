package com.github.openstream.core.datastore

import com.github.openstream.core.media3.PlayerRepeatMode
import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.serialization.Serializable

// queue is what the player plays from, it can be shuffled and the original queue holds the unshuffled order

@Serializable
data class QueueModel(
    val currentVideoIndex: Int? = null,
    val queue: List<VideoItem> = emptyList(),
    val originalQueue: List<VideoItem> = emptyList(),
    val isShuffleEnabled: Boolean = false,
    val repeatMode: PlayerRepeatMode = PlayerRepeatMode.ALL,
)
