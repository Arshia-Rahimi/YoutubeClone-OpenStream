package com.github.openstream.core.database.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["playlistId", "channelId"],
    tableName = "channel_playlist",
    indices = [Index("channelId"), Index("playlistId")],
)
data class ChannelPlaylistCrossRef(
    val playlistId: Long,
    val channelId: Long,
)
