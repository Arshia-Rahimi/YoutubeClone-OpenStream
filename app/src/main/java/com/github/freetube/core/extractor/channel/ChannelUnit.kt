package com.github.freetube.core.extractor.channel

import com.github.freetube.core.extractor.YtService
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.model.toList
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

const val Channel_Base_Url = "https://youtube.com/channel/"

class ChannelUnit(
    val url: String,
) {
    private val channelExtractor = YtService.getChannelExtractor(url)
    private val tabExtractors: MutableMap<ChannelTabExtractor, Page?>
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
                    url = it.baseUrl,
                )
            },
        )
        tabExtractors = buildMap<ChannelTabExtractor, Page?> {
            data.tabs.forEach {
                try {
                    val url = Channel_Base_Url + channelExtractor.id + "/" + it.name
                    val tab =
                        YtService.getChannelTabExtractor(YtService.channelTabLHFactory.fromUrl(url))
                    tab?.let { put(it, null) }
                } catch (e: Exception) {
                }
            }
        }.toMutableMap()
    }

    private fun getTabExtractor(tab: ChannelTab) =
        tabExtractors.entries.first { it.key.url == Channel_Base_Url + channelExtractor.id + "/" + tab.name }

    fun fetchTab(tab: ChannelTab): List<DataItem>? {
        val extractor = getTabExtractor(tab).key
        extractor.fetchPage()
        tabExtractors[extractor] = extractor.initialPage.nextPage
        return extractor.initialPage.items.toList()
    }

    fun fetchNextPage(tab: ChannelTab): List<DataItem>? {
        val tab = getTabExtractor(tab)
        val currentPage = tab.key.getPage(tab.value)
        tabExtractors[tab.key] = currentPage.nextPage
        return currentPage.items.toList()
    }
}
