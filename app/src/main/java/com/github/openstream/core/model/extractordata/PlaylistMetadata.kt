package com.github.openstream.core.model.extractordata

data class PlaylistMetadata(
    val name: String,
    val channelName: String?,
    val channelUrl: String?,
    val isChannelVerified: Boolean?,
    val count: Long,
)