package com.github.openstream.core.database.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["playlistId", "videoId"],
    tableName = "playlist_video",
    indices = [Index("playlistId")],
)
data class PlaylistVideoCrossRef(
    val playlistId: Long,
    val videoId: Long,
)
