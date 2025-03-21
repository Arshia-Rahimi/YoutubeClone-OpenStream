package com.github.freetube.core.extractor.channel

data class ChannelInfo(
    val name: String,
    val subscriberCount: Long,
    val description: String,
    val thumbnails: String,
    val verified: Boolean,
    val banner: String,
    val tabs: List<ChannelTab>,
    val id: String,
)

data class ChannelTab(
    val name: String,
    val url: String,
)
