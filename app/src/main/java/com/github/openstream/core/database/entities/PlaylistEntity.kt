package com.github.openstream.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.openstream.core.database.OpenStreamEntity
import com.github.openstream.core.model.extractordata.DataItem

@Entity("playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val count: Long,
    val channelName: String? = null,
    val channelUrl: String? = null,
    val isChannelVerified: Boolean? = null,
    val thumbnail: String? = null,
    val url: String? = null,
) : OpenStreamEntity {
    fun toDataItem(): DataItem.Playlist =
        when {
            url == null -> DataItem.Playlist.LocalPlaylist(
                name = name,
                thumbnail = thumbnail,
                count = count,
                id = id,
            )
            
            else -> DataItem.Playlist.OfflineFirstPlaylist(
                name = name,
                url = url,
                thumbnail = thumbnail,
                channelUrl = channelUrl ?: "",
                channelName = channelName ?: "",
                count = count,
                isChannelVerified = isChannelVerified == true,
                id = id,
            )
        }
}
