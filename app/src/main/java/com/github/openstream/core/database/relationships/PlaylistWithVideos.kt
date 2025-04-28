package com.github.openstream.core.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.VideoEntity

data class PlaylistWithVideos(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId",
    )
    val videos: List<VideoEntity>,
)
