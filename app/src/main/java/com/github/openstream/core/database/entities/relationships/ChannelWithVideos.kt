package com.github.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.github.openstream.core.database.entities.ChannelEntity
import com.github.openstream.core.database.entities.ChannelVideoCrossRef
import com.github.openstream.core.database.entities.VideoEntity

data class ChannelWithVideos(
    @Embedded val channel: ChannelEntity,
    @Relation(
        parentColumn = "channelId",
        entityColumn = "videoId",
        associateBy = Junction(ChannelVideoCrossRef::class),
    )
    val playlists: List<VideoEntity>,
)
