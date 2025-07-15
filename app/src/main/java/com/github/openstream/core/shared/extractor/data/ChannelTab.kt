package com.github.openstream.core.shared.extractor.data

import kotlinx.serialization.Serializable

@Serializable
data class ChannelTab(
    val name: String,
    val url: String,
)
