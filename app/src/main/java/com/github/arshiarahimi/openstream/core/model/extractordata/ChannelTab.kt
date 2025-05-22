package com.github.arshiarahimi.openstream.core.model.extractordata

import kotlinx.serialization.Serializable

@Serializable
data class ChannelTab(
    val name: String,
    val url: String,
)

data class ChannelTabView(
    val name: String,
    val url: String,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    fun toChannelTab() = ChannelTab(name, url)
}
