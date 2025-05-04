package com.github.openstream.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.openstream.core.database.OpenStreamEntity
import com.github.openstream.core.model.extractordata.StreamType

@Entity("videos")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true) val videoId: Long = 0,
    val url: String,
    val name: String,
    val thumbnail: String?,
    @ColumnInfo("view_count") val viewCount: Long,
    @ColumnInfo("upload_date") val uploadDate: String,
    @ColumnInfo("stream_type") val streamType: StreamType,
    val duration: Long,
    @ColumnInfo("channel_name") val channelName: String,
    @ColumnInfo("channel_url") val channelUrl: String?,
    @ColumnInfo("is_channel_verified") val isChannelVerified: Boolean,
): OpenStreamEntity
