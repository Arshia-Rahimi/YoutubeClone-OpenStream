package com.github.arshiarahimi.openstream.core.model.extractordata

import com.github.arshiarahimi.openstream.core.database.Entityable
import com.github.arshiarahimi.openstream.core.database.entities.ChannelEntity
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

sealed interface Channel : Entityable, ViewableObject {
    val url: String
    val metadata: ChannelMetadata
    val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>?

    override fun toEntity(): ChannelEntity
}

data class OnlineChannel(
    override val metadata: ChannelMetadata,
    override val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>,
    override val url: String
) : Channel {
    override fun toEntity() = ChannelEntity(
        name = metadata.name,
        url = url,
        avatar = metadata.avatar,
        subscriberCount = metadata.subscriberCount,
        isVerified = metadata.isVerified,
        description = metadata.description,
    )

    fun toOfflineFirstChannel(id: Long) = OfflineFirstChannel(
        url = url,
        metadata = metadata,
        tabExtractors = tabExtractors,
        id = id,
    )
}

data class OfflineFirstChannel(
    override val url: String,
    override val metadata: ChannelMetadata,
    override val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>? = null,
    val id: Long,
) : Channel {
    override fun toEntity() = ChannelEntity(
        channelId = id,
        name = metadata.name,
        url = url,
        avatar = metadata.avatar,
        subscriberCount = metadata.subscriberCount,
        isVerified = metadata.isVerified,
        description = metadata.description,
    )
}
