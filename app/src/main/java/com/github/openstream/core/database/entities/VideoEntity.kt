package com.github.openstream.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.openstream.core.database.OpenStreamEntity
import com.github.openstream.core.model.extractordata.StreamType

@Entity("videos")
data class VideoEntity(
    val id: Long,
    val url: String,
    val name: String,
    val thumbnail: String?,
    val viewCount: Long,
    val uploadDate: String,
    val streamType: StreamType,
    val duration: Long,
    val channelName: String,
    val channelUrl: String?,
    val isChannelVerified: Boolean,
): OpenStreamEntity
