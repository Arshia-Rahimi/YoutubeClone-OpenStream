package com.github.openstream.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.openstream.core.shared.dataitem.OpenStreamEntity
import com.github.openstream.core.shared.dataitem.StreamType
import com.github.openstream.core.shared.dataitem.VideoItem

@Entity("videos", indices = [Index(value = ["url"], unique = true)])
data class VideoEntity(
    @PrimaryKey(autoGenerate = true) val videoId: Long = 0,
    val url: String,
    val name: String,
    val thumbnail: String?,
    @ColumnInfo("view_count") val viewCount: Long,
    @ColumnInfo("upload_date") val uploadDate: Long?,
    @ColumnInfo("stream_type") val streamType: StreamType,
    val duration: Long,
    @ColumnInfo("channel_name") val channelName: String,
    @ColumnInfo("channel_url") val channelUrl: String,
    @ColumnInfo("is_channel_verified") val isChannelVerified: Boolean,
) : OpenStreamEntity {
    fun toDataItem() = VideoItem(
        name = name,
        thumbnail = thumbnail,
        url = url,
        streamType = streamType,
        channelUrl = channelUrl,
        channelName = channelName,
        shortDescription = "",
        duration = duration,
        viewCount = viewCount,
        isChannelVerified = isChannelVerified,
        uploadDate = uploadDate,
        channelAvatars = "",
        id = videoId,
        isShort = false,
    )
}
