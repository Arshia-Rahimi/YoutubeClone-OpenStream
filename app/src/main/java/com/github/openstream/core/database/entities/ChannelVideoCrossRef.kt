package com.github.openstream.core.database.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["channelId", "videoId"],
    tableName = "channel_video",
    indices = [Index("channelId")],
)
data class ChannelVideoCrossRef(
    val channelId: Long,
    val videoId: Long,
)
