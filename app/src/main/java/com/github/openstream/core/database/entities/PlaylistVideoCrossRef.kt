package com.github.openstream.core.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "videoUrl"])
data class PlaylistVideoCrossRef(
    val playlistId: Long,
    val videoId: Long,
)
