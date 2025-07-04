package com.github.openstream.core.datastore

import com.github.openstream.core.media3.PlayerRepeatMode
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDataModel(
    val seekIncrement: Long = 10000L,
    val playbackSpeed: Float = 1f,
    val repeatMode: PlayerRepeatMode = PlayerRepeatMode.ALL,
)
