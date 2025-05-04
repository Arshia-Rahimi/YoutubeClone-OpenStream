package com.github.openstream.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.openstream.core.database.OpenStreamEntity

@Entity("channels")
data class ChannelEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("channel_id") val channelId: Long = 0,
    val name: String,
    val url: String? = null,
    val avatar: String? = null,
    @ColumnInfo(name = "subscriber_count") val subscriberCount: Long,
    @ColumnInfo("is_verified") val isVerified: Boolean,
    val banner: String,
    val description: String,
): OpenStreamEntity
