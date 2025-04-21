package com.github.openstream.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.openstream.core.model.extractordata.StreamType

@Entity("videos")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    val name: String,
    val url: String? = null,
    val thumbnail: String,
    val viewCount: Long,
    val uploadDate: String,
    val playlistId: Int,
    val streamType: StreamType,
    val duration: Long,
    val channelName: String,
    val channelUrl: String?,
    val isChannelVerified: Boolean,
)
