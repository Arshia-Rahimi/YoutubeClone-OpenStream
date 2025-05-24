package com.github.arshiarahimi.openstream.core.model.extractordata

import kotlinx.serialization.Serializable

@Serializable
data class ChannelTab(
    val name: String,
    val url: String,
)
