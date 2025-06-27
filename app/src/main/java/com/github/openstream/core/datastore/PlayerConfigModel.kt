package com.github.openstream.core.datastore

import com.github.openstream.core.media3.PlayerRepeatMode
import kotlinx.serialization.Serializable

@Serializable
data class PlayerConfigModel(
    val seekIncrement: Long = 10000L,
    val playerRepeatMode: PlayerRepeatMode = PlayerRepeatMode.OFF,
    val isShuffleEnabled: Boolean = false,
    val playbackSpeed: Float = 1F,
)
