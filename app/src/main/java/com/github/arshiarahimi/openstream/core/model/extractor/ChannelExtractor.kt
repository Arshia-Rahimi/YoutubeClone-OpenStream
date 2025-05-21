package com.github.arshiarahimi.openstream.core.model.extractor

import com.github.arshiarahimi.openstream.core.database.entities.ChannelEntity
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelMetadata
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

data class ChannelExtractor(
    val url: String,
    val metadata: ChannelMetadata,
    val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>,
) : Entityable {
    override fun toEntity() = ChannelEntity(
        name = metadata.name,
        url = url,
        avatar = metadata.avatar,
        subscriberCount = metadata.subscriberCount,
        isVerified = metadata.isVerified,
        description = metadata.description,
    )
}
