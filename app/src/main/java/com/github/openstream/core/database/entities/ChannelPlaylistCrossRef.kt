package com.github.openstream.core.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "channelId"], tableName = "channel_playlist")
data class ChannelPlaylistCrossRef(
    val playlistId: Long,
    val channelId: Long,
)