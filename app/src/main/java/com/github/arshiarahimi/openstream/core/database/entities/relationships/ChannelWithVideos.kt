package com.github.arshiarahimi.openstream.core.database.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.github.arshiarahimi.openstream.core.database.entities.ChannelEntity
import com.github.arshiarahimi.openstream.core.database.entities.VideoEntity
import com.github.arshiarahimi.openstream.core.database.entities.crossrefs.ChannelVideoCrossRef
import com.github.arshiarahimi.openstream.core.model.extractor.OfflineFirstChannelExtractor
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelMetadata

data class ChannelWithVideos(
    @Embedded val channel: ChannelEntity,
    @Relation(
        parentColumn = "channelId",
        entityColumn = "videoId",
        associateBy = Junction(ChannelVideoCrossRef::class),
    )
    val videos: List<VideoEntity>,
) {
    fun toObject() = OfflineFirstChannelExtractor(
        url = channel.url,
        id = channel.channelId,
        metadata = ChannelMetadata(
            name = channel.name,
            subscriberCount = channel.subscriberCount,
            description = channel.description,
            avatar = channel.avatar,
            isVerified = channel.isVerified,
            tabs = channel.tabs,
        ),
    )
}
