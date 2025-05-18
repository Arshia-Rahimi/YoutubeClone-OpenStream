package com.github.arshiarahimi.openstream.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.arshiarahimi.openstream.core.database.OpenStreamEntity
import com.github.arshiarahimi.openstream.core.model.extractordata.PlaylistItem

@Entity("playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val playlistId: Long = 0,
    val name: String,
    val count: Long,
    @ColumnInfo("channel_name") val channelName: String? = null,
    @ColumnInfo("channel_url") val channelUrl: String? = null,
    @ColumnInfo("is_channel_verified") val isChannelVerified: Boolean? = null,
    val thumbnail: String? = null,
    val url: String? = null,
) : OpenStreamEntity {
    fun toDataItem(): PlaylistItem =
        when {
            url == null -> PlaylistItem.LocalOnlyPlaylistItem(
                name = name,
                thumbnail = thumbnail,
                count = count,
                id = playlistId,
            )

            else -> PlaylistItem.OfflineFirstPlaylistItem(
                name = name,
                url = url,
                thumbnail = thumbnail,
                channelUrl = channelUrl ?: "",
                channelName = channelName ?: "",
                count = count,
                isChannelVerified = isChannelVerified == true,
                id = playlistId,
            )
        }
}
