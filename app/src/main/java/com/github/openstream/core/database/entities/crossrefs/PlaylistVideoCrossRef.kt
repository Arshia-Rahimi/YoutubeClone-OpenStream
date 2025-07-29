package com.github.openstream.core.database.entities.crossrefs

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["playlistId", "videoId"],
    tableName = "playlist_video",
    indices = [Index("playlistId"), Index("videoId")],
)
data class PlaylistVideoCrossRef(
    val playlistId: Long,
    val videoId: Long,
    val timestamp: Long = System.currentTimeMillis(),
)