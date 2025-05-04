package com.github.openstream.core.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "videoId"], tableName = "playlist_video")
data class PlaylistVideoCrossRef(
    val playlistId: Long,
    val videoId: Long,
)
