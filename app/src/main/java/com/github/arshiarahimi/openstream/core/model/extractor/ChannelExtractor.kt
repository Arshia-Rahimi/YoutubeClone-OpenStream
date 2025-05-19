package com.github.arshiarahimi.openstream.core.model.extractor

import com.github.arshiarahimi.openstream.core.database.entities.ChannelEntity
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelMetadata
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

sealed interface ChannelExtractor : Entityable {
    val url: String
    val metadata: ChannelMetadata
    val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>?

    override fun toEntity(): ChannelEntity
}

data class OnlineChannelExtractor(
    override val metadata: ChannelMetadata,
    override val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>,
    override val url: String
) : ChannelExtractor {
    override fun toEntity() = ChannelEntity(
        name = metadata.name,
        url = url,
        avatar = metadata.avatar,
        subscriberCount = metadata.subscriberCount,
        isVerified = metadata.isVerified,
        description = metadata.description,
    )

    fun toOfflineFirstChannel(id: Long) = OfflineFirstChannelExtractor(
        url = url,
        metadata = metadata,
        tabExtractors = tabExtractors,
        id = id,
    )
}

data class OfflineFirstChannelExtractor(
    override val url: String,
    override val metadata: ChannelMetadata,
    override val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>? = null,
    val id: Long,
) : ChannelExtractor {
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
