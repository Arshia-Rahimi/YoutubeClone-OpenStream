package com.github.freetube.core.datastore.proto.playerconfig

import com.github.freetube.core.media3.PlayerRepeatMode
import kotlinx.serialization.Serializable

@Serializable
data class PlayerConfigDataStoreModel(
    val seekIncrement: Long = 10000L,
    val playlistRepeatMode: PlayerRepeatMode = PlayerRepeatMode.OFF,
    val playlistShuffleEnabled: Boolean = false,
    val playbackSpeed: Float = 1F,
)


