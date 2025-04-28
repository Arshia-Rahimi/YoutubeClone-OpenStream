package com.github.openstream.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.openstream.core.database.OpenStreamEntity

@Entity("playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    val name: String,
    val channelName: String? = null,
    val channelUrl: String? = null,
    val isChannelVerified: Boolean? = null,
    val count: Long,
    val thumbnail: String? = null,
    val url: String? = null,
): OpenStreamEntity
