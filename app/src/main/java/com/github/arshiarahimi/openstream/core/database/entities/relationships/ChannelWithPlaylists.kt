package com.github.arshiarahimi.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.github.arshiarahimi.openstream.core.database.entities.ChannelEntity
import com.github.arshiarahimi.openstream.core.database.entities.ChannelPlaylistCrossRef
import com.github.arshiarahimi.openstream.core.database.entities.PlaylistEntity

data class ChannelWithPlaylists(
    @Embedded val channel: ChannelEntity,
    @Relation(
        parentColumn = "channelId",
        entityColumn = "playlistId",
        associateBy = Junction(ChannelPlaylistCrossRef::class),
    )
    val playlists: List<PlaylistEntity>,
)
