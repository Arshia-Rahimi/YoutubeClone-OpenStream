package com.github.openstream.core.datastore

import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.serialization.Serializable

@Serializable
data class QueueModel(
    val currentVideoIndex: Int? = null,
    val queue: List<VideoItem> = emptyList(),
)
