package com.github.openstream.core.model.extractordata

data class ChannelMetadata(
    val name: String,
    val subscriberCount: Long,
    val description: String,
    val avatar: String,
    val isVerified: Boolean,
    val banner: String,
    val tabs: List<ChannelTab>,
    val id: String,
)

data class ChannelTab(
    val name: String,
    val url: String,
    var isLoading: Boolean = true,
    var error: String? = null,
)
