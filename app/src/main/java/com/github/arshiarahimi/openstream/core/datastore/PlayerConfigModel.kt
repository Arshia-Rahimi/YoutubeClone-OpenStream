package com.github.arshiarahimi.openstream.core.datastore

import com.github.arshiarahimi.openstream.core.media3.PlayerRepeatMode
import kotlinx.serialization.Serializable

@Serializable
data class PlayerConfigModel(
    val seekIncrement: Long = 10000L,
    val playerRepeatMode: PlayerRepeatMode = PlayerRepeatMode.OFF,
    val isPlaylistShuffleEnabled: Boolean = false,
    val playbackSpeed: Float = 1F,
)
