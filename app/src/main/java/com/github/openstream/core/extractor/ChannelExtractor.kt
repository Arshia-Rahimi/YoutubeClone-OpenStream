package com.github.openstream.core.extractor

import com.github.openstream.core.extractor.util.YtService
import com.github.openstream.core.model.extractordata.Channel
import com.github.openstream.core.model.extractordata.ChannelMetadata
import com.github.openstream.core.model.extractordata.ChannelTab
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.OnlineChannel
import com.github.openstream.core.model.extractordata.toListOfDataItem
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeChannelTabLinkHandlerFactory

object ChannelExtractor {
    
    fun getChannelData(url: String): OnlineChannel {
        val channelExtractor = YtService.getChannelExtractor(url)
        channelExtractor.fetchPage()
        val channelMetadata = ChannelMetadata(
            name = channelExtractor.name,
            subscriberCount = channelExtractor.subscriberCount,
            description = channelExtractor.description,
            avatar = channelExtractor.avatars.first().url,
            isVerified = channelExtractor.isVerified,
            banner = channelExtractor.banners.first().url,
            id = channelExtractor.id,
            tabs = channelExtractor.tabs.map {
                ChannelTab(
                    name = it.url.split("/").last().lowercase()
                        .let { last -> if (last == "streams") "livestreams" else last },
                    url = it.url,
                )
            },
        )
        return OnlineChannel(
            metadata = channelMetadata,
            tabExtractors = buildList<Triple<String, ChannelTabExtractor, Page?>> {
                channelMetadata.tabs.forEach {
                    try {
                        val url = "channel/" + channelExtractor.id
                        var name = it.url.split("/").last().lowercase()
                            .let { last -> if (last == "streams") "livestreams" else last }
                        val tab = YoutubeChannelTabExtractor(
                            YtService,
                            YoutubeChannelTabLinkHandlerFactory.getInstance()
                                .fromQuery(url, listOf(name), null)
                        )
                        add(Triple(name, tab, null))
                    } catch (e: Exception) {
                    }
                }
            }.toMutableList(),
            url = url,
        )
    }
    
    fun fetchTab(channel: Channel, tab: ChannelTab): List<DataItem>? {
        val extractor = channel.tabExtractors.first { it.first == tab.name.lowercase() }
        extractor.second.fetchPage()
        val index = channel.tabExtractors.indexOfFirst { it.first == extractor.first }
        channel.tabExtractors[index] =
            Triple(
                extractor.first, extractor.second,
                if (extractor.second.initialPage.hasNextPage())
                    extractor.second.initialPage.nextPage
                else null
            )
        return extractor.second.initialPage.items.toListOfDataItem()
    }
    
    fun fetchNextPage(channel: Channel, tab: ChannelTab): List<DataItem>? {
        val tab = channel.tabExtractors.first { it.first == tab.name.lowercase() }
        return tab.third?.let {
            val currentPage = tab.second.getPage(it)
            val index = channel.tabExtractors.indexOfFirst { it.first == tab.first }
            channel.tabExtractors[index] = Triple(tab.first, tab.second, currentPage.nextPage)
            currentPage.items.toListOfDataItem()
        }
    }
}