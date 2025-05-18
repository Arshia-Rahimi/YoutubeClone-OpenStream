package com.github.arshiarahimi.openstream.core.database.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["channelId", "videoId"],
    tableName = "channel_video",
    indices = [Index("channelId"), Index("videoId")],
)
data class ChannelVideoCrossRef(
    val channelId: Long,
    val videoId: Long,
)
