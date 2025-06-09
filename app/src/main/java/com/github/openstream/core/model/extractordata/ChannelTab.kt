package com.github.openstream.core.model.extractordata

import kotlinx.serialization.Serializable

@Serializable
data class ChannelTab(
    val name: String,
    val url: String,
)
