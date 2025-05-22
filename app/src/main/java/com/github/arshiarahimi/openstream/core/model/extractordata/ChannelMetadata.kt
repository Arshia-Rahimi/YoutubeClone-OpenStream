package com.github.arshiarahimi.openstream.core.model.extractordata

import kotlinx.serialization.Serializable

data class ChannelMetadata(
    val name: String,
    val subscriberCount: Long,
    val description: String,
    val avatar: String,
    val isVerified: Boolean,
    val tabs: List<ChannelTab>? = null,
)

@Serializable
data class ChannelTab(
    val name: String,
    val url: String,
)
