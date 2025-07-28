package com.github.openstream.core.extractor.datasource

import com.github.openstream.core.extractor.YtService
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.DataItem
import com.github.openstream.core.shared.dataitem.toListOfDataItem
import com.github.openstream.core.shared.extractor.ChannelExtractor
import com.github.openstream.core.shared.extractor.data.ChannelTab
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeChannelTabLinkHandlerFactory

object ChannelRemoteDataSource {

    fun getChannelData(url: String): ChannelExtractor {
        val channelExtractor = YtService.getChannelExtractor(url)
        channelExtractor.fetchPage()
        val tabs = channelExtractor.tabs.map {
            ChannelTab(
                name = it.url.split("/").last().lowercase()
                    .let { last -> if (last == "streams") "livestreams" else last },
                url = it.url,
            )
        }
        return ChannelExtractor(
            tabs = tabs,
            tabExtractors = buildList<Triple<String, ChannelTabExtractor, Page?>> {
                tabs.forEach {
                    val url = "channel/" + channelExtractor.id
                    val name = when(val n = it.url.split("/").last().lowercase()) {
                        "streams" -> "livestreams"
                        "releases" -> "albums"
                        else -> n
                    }
                    val tab = YoutubeChannelTabExtractor(
                        YtService,
                        YoutubeChannelTabLinkHandlerFactory.getInstance()
                            .fromQuery(url, listOf(name), null)
                    )
                    add(Triple(it.name, tab, null))
                }
            }.toMutableList(),
            url = url,
            channelItem = ChannelItem.OnlineChannelItem(
                name = channelExtractor.name,
                subscriberCount = channelExtractor.subscriberCount,
                description = channelExtractor.description,
                avatar = channelExtractor.avatars.last().url,
                isVerified = channelExtractor.isVerified,
                url = url,
            )
        )
    }

    fun fetchTab(channel: ChannelExtractor, tab: ChannelTab): List<DataItem>? {
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

    fun fetchNextPage(channel: ChannelExtractor, tab: ChannelTab): List<DataItem>? {
        val tab = channel.tabExtractors.first { it.first == tab.name.lowercase() }
        return tab.third?.let {
            val currentPage = tab.second.getPage(it)
            val index = channel.tabExtractors.indexOfFirst { tab -> tab.first == tab.first }
            channel.tabExtractors[index] = Triple(tab.first, tab.second, currentPage.nextPage)
            currentPage.items.toListOfDataItem()
        }
    }
}
