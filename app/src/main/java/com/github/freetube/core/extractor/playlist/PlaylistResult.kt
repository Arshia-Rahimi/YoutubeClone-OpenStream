package com.github.freetube.core.extractor.playlist

import com.github.freetube.core.extractor.model.DataItem

data class PlaylistResult(
    val name: String,
    val items: List<DataItem>,
    val channelName: String,
    val channelUrl: String,
    val description: String,
    val isChannelVerified: Boolean,
    val count: Long,
)
