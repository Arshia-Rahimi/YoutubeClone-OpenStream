package com.github.openstream.core.shared.extractor

import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.extractor.data.ChannelTab
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

data class ChannelExtractor(
    val url: String,
    val tabs: List<ChannelTab>,
    val channelItem: ChannelItem,
    val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>,
)
