package com.github.openstream.core.model.extractordata

import com.github.openstream.core.extractor.util.YtService
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeChannelTabLinkHandlerFactory

class ChannelUnit(
    val url: String,
) {
    private val channelExtractor = YtService.getChannelExtractor(url)
    val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>
    val data: ChannelInfo

    init {
        channelExtractor.fetchPage()
        data = ChannelInfo(
            name = channelExtractor.name,
            subscriberCount = channelExtractor.subscriberCount,
            description = channelExtractor.description,
            avatar = channelExtractor.avatars.first().url,
            verified = channelExtractor.isVerified,
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
        tabExtractors = buildList<Triple<String, ChannelTabExtractor, Page?>> {
            data.tabs.forEach {
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
        }.toMutableList()
    }

    private fun getTabExtractor(tab: ChannelTab) =
        tabExtractors.first { it.first == tab.name.lowercase() }

    fun fetchTab(tab: ChannelTab): List<DataItem>? {
        val extractor = getTabExtractor(tab)
        extractor.second.fetchPage()
        val index = tabExtractors.indexOfFirst { it.first == extractor.first }
        tabExtractors[index] =
            Triple(
                extractor.first, extractor.second,
                if (extractor.second.initialPage.hasNextPage())
                    extractor.second.initialPage.nextPage
                else null
            )
        return extractor.second.initialPage.items.toListOfDataItem()
    }

    fun fetchNextPage(tab: ChannelTab): List<DataItem>? {
        val tab = getTabExtractor(tab)
        return tab.third?.let {
            val currentPage = tab.second.getPage(it)
            val index = tabExtractors.indexOfFirst { it.first == tab.first }
            tabExtractors[index] = Triple(tab.first, tab.second, currentPage.nextPage)
            currentPage.items.toListOfDataItem()
        }
    }
}
