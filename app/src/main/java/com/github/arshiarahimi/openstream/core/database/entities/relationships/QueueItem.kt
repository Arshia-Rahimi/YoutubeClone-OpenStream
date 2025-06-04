package com.github.arshiarahimi.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.github.arshiarahimi.openstream.core.database.entities.QueueItemEntity
import com.github.arshiarahimi.openstream.core.database.entities.VideoEntity

data class QueueItem(
    @Embedded val video: VideoEntity,
    @Relation(
        parentColumn = "videoId",
        entityColumn = "videoId",
    )
    val order: QueueItemEntity,
)
