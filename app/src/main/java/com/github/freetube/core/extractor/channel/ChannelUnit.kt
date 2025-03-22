package com.github.freetube.core.extractor.channel

import com.github.freetube.core.extractor.YtService
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.model.toList
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeChannelTabExtractor
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeChannelTabLinkHandlerFactory

const val Channel_Base_Url = "https://youtube.com/channel/"

class ChannelUnit(
    val url: String,
) {
    private val channelExtractor = YtService.getChannelExtractor(url)
    private val tabExtractors: MutableList<Triple<String, ChannelTabExtractor, Page?>>
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
                    name = it.url.split("/").last(),
                    url = it.url,
                )
            },
        )
        tabExtractors = buildList<Triple<String, ChannelTabExtractor, Page?>> {
            data.tabs.forEach {
//                try {
                val url = "channel/" + channelExtractor.id
                var name = it.name.lowercase()
                name = if (name == "streams") "livestreams" else name
                val tab = YoutubeChannelTabExtractor(
                    YtService,
                    YoutubeChannelTabLinkHandlerFactory.getInstance()
                        .fromQuery(url, listOf(name), null)
                )
                add(Triple(name, tab, null))
//                } catch (e: Exception) { println(e.message) }
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
            Triple(extractor.first, extractor.second, extractor.second.initialPage.nextPage)
        return extractor.second.initialPage.items.toList()
    }

    fun fetchNextPage(tab: ChannelTab): List<DataItem>? {
        val tab = getTabExtractor(tab)
        val currentPage = tab.second.getPage(tab.third)
        val index = tabExtractors.indexOfFirst { it.first == tab.first }
        tabExtractors[index] = Triple(tab.first, tab.second, currentPage.nextPage)
        return currentPage.items.toList()
    }
}
