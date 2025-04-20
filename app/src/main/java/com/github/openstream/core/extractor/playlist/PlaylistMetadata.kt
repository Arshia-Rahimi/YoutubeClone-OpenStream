package com.github.openstream.core.extractor.playlist

data class PlaylistMetadata(
    val name: String,
    val channelName: String,
    val channelUrl: String,
    val description: String,
    val isChannelVerified: Boolean,
    val count: Long,
)
