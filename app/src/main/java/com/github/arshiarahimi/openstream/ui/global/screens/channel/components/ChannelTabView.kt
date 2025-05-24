package com.github.arshiarahimi.openstream.ui.global.screens.channel.components

import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTab

data class ChannelTabView(
    val name: String,
    val url: String,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    fun toChannelTab() = ChannelTab(name, url)
}
