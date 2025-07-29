package com.github.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.VideoEntity
import com.github.openstream.core.database.entities.crossrefs.PlaylistVideoCrossRef

data class PlaylistWithVideos(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "videoId",
        associateBy = Junction(PlaylistVideoCrossRef::class),
    )
    val videos: List<VideoEntity>,
)

data class PlaylistWithVideosWithPivot(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "videoId",
        associateBy = Junction(PlaylistVideoCrossRef::class),
    )
    val videos: List<VideoEntity>,
    @Embedded(prefix = "pivot_") val pivot: PlaylistVideoCrossRef
)
