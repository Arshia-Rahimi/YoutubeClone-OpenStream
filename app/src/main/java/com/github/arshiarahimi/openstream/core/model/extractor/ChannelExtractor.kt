package com.github.arshiarahimi.openstream.core.model.extractor

import com.github.arshiarahimi.openstream.core.model.dataitem.ChannelItem
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTab
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

data class ChannelExtractor(
    val url: String,
    val tabs: List<ChannelTab>,
    val channelItem: ChannelItem,
    val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>,
)
