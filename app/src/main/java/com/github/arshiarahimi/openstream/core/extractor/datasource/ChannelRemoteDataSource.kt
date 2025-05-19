package com.github.arshiarahimi.openstream.core.extractor.datasource

import com.github.arshiarahimi.openstream.core.extractor.YtService
import com.github.arshiarahimi.openstream.core.model.dataitem.DataItem
import com.github.arshiarahimi.openstream.core.model.dataitem.toListOfDataItem
import com.github.arshiarahimi.openstream.core.model.extractor.OnlineChannelExtractor
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelMetadata
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelTab
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeChannelTabLinkHandlerFactory

object ChannelRemoteDataSource {

    fun getChannelData(url: String): OnlineChannelExtractor {
        val channelExtractor = YtService.getChannelExtractor(url)
        channelExtractor.fetchPage()
        val channelMetadata = ChannelMetadata(
            name = channelExtractor.name,
            subscriberCount = channelExtractor.subscriberCount,
            description = channelExtractor.description,
            avatar = channelExtractor.avatars.first().url,
            isVerified = channelExtractor.isVerified,
            tabs = channelExtractor.tabs.map {
                ChannelTab(
                    name = it.url.split("/").last().lowercase()
                        .let { last -> if (last == "streams") "livestreams" else last },
                    url = it.url,
                )
            },
        )
        return OnlineChannelExtractor(
            metadata = channelMetadata,
            tabExtractors = buildList<Triple<String, ChannelTabExtractor, Page?>> {
                channelMetadata.tabs?.forEach {
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

    fun fetchTab(channel: OnlineChannelExtractor, tab: ChannelTab): List<DataItem>? {
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

    fun fetchNextPage(channel: OnlineChannelExtractor, tab: ChannelTab): List<DataItem>? {
        val tab = channel.tabExtractors.first { it.first == tab.name.lowercase() }
        return tab.third?.let {
            val currentPage = tab.second.getPage(it)
            val index = channel.tabExtractors.indexOfFirst { it.first == tab.first }
            channel.tabExtractors[index] = Triple(tab.first, tab.second, currentPage.nextPage)
            currentPage.items.toListOfDataItem()
        }
    }
}
