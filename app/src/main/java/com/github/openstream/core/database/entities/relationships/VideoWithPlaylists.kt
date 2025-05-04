package com.github.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.github.openstream.core.database.entities.VideoEntity

data class VideoWithPlaylists(
    @Embedded val video: VideoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId",
    )
    val videos: List<VideoEntity>,
)
