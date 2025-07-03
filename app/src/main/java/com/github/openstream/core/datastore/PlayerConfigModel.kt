package com.github.openstream.core.datastore

import kotlinx.serialization.Serializable

@Serializable
data class PlayerConfigModel(
    val seekIncrement: Long = 10000L,
    val playbackSpeed: Float = 1F,
)
