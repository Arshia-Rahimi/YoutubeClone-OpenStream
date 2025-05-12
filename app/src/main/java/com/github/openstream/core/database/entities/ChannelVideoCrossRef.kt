package com.github.openstream.core.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["channelId", "videoId"], tableName = "channel_video")
data class ChannelVideoCrossRef(
    val channelId: Long,
    val videoId: Long,
)
