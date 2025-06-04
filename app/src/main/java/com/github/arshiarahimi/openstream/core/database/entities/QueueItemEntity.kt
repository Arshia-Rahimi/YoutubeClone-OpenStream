package com.github.arshiarahimi.openstream.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queue_order")
data class QueueItemEntity(
    @PrimaryKey val videoId: Long,
    val nextVideoId: Long?,
)
