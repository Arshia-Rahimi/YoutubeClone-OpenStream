package com.github.openstream.core.model.extractor

import com.github.openstream.core.model.dataitem.ChannelItem
import com.github.openstream.core.model.extractordata.ChannelTab
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

data class ChannelExtractor(
    val url: String,
    val tabs: List<ChannelTab>,
    val channelItem: ChannelItem,
    val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>,
)
