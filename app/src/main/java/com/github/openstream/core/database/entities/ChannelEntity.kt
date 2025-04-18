package com.github.openstream.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("channels")
data class ChannelEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    val name: String,
    val url: String? = null,
    val avatar: String,
    @ColumnInfo(name = "subscriber_count") val subscriberCount: Long,
    val isVerified: Boolean,
    val banner: String,
    val description: String,
)
