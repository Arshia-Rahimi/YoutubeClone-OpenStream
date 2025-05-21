package com.github.arshiarahimi.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.github.arshiarahimi.openstream.core.database.entities.ChannelEntity
import com.github.arshiarahimi.openstream.core.database.entities.VideoEntity
import com.github.arshiarahimi.openstream.core.database.entities.crossrefs.ChannelVideoCrossRef

data class ChannelWithVideos(
    @Embedded val channel: ChannelEntity,
    @Relation(
        parentColumn = "channelId",
        entityColumn = "videoId",
        associateBy = Junction(ChannelVideoCrossRef::class),
    )
    val videos: List<VideoEntity>,
) 
