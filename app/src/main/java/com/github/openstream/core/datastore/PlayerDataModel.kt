package com.github.openstream.core.datastore

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDataModel(
    val seekIncrement: Long = 10000L,
    val playbackSpeed: Float = 1f,
)
