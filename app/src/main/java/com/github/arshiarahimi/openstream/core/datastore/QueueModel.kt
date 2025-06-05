package com.github.arshiarahimi.openstream.core.datastore

import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import kotlinx.serialization.Serializable

@Serializable
data class QueueModel(
    val queue: List<VideoItem> = emptyList(),
)
