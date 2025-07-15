package com.github.openstream.ui.global.screens.channel.model

import com.github.openstream.core.shared.extractor.data.ChannelTab

data class ChannelTabView(
    val name: String,
    val url: String,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    fun toChannelTab() = ChannelTab(name, url)
}
