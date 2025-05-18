package com.github.arshiarahimi.openstream.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.arshiarahimi.openstream.core.database.OpenStreamEntity
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelItem
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTab

@Entity("channels", indices = [Index(value = ["url"], unique = true)])
data class ChannelEntity(
    @PrimaryKey(autoGenerate = true) val channelId: Long = 0,
    val name: String,
    val url: String,
    val avatar: String,
    @ColumnInfo(name = "subscriber_count") val subscriberCount: Long,
    @ColumnInfo("is_verified") val isVerified: Boolean,
    val description: String,
    val tabs: List<ChannelTab>? = null,
) : OpenStreamEntity {
    fun toDataItem() = ChannelItem.OfflineFirstChannelItem(
        id = channelId,
        name = name,
        url = url,
        avatar = avatar,
        subscriberCount = subscriberCount,
        isVerified = isVerified,
        description = description,
    )
}
