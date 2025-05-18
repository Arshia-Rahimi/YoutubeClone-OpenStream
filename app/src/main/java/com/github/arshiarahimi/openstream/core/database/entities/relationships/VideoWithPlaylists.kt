package com.github.arshiarahimi.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.github.arshiarahimi.openstream.core.database.entities.PlaylistEntity
import com.github.arshiarahimi.openstream.core.database.entities.PlaylistVideoCrossRef
import com.github.arshiarahimi.openstream.core.database.entities.VideoEntity

data class VideoWithPlaylists(
    @Embedded val video: VideoEntity,
    @Relation(
        parentColumn = "videoId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistVideoCrossRef::class),
    )
    val playlists: List<PlaylistEntity>,
)
