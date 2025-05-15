package com.github.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.github.openstream.core.database.entities.ChannelEntity
import com.github.openstream.core.database.entities.ChannelVideoCrossRef
import com.github.openstream.core.database.entities.VideoEntity
import com.github.openstream.core.model.extractordata.ChannelMetadata
import com.github.openstream.core.model.extractordata.OfflineFirstChannel

data class ChannelWithVideos(
    @Embedded val channel: ChannelEntity,
    @Relation(
        parentColumn = "channelId",
        entityColumn = "videoId",
        associateBy = Junction(ChannelVideoCrossRef::class),
    )
    val videos: List<VideoEntity>,
) : Objectable {
    override fun toObject() = OfflineFirstChannel(
        url = channel.url,
        id = channel.channelId,
        metadata = ChannelMetadata(
            name = channel.name,
            subscriberCount = channel.subscriberCount,
            description = channel.description,
            avatar = channel.avatar,
            isVerified = channel.isVerified,
            tabs = channel.tabs,
            id = channel.channelId,
        ),
    )
}
