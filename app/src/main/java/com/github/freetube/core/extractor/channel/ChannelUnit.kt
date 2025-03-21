package com.github.freetube.core.extractor.channel

import com.github.freetube.core.extractor.YtService
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

const val Channel_Base_Url = "https://youtube.com/channel"

class ChannelUnit(
    val url: String,
) {
    private val extractor = YtService.getChannelExtractor(url)
    private val tabExtractors: List<ChannelTabExtractor>
    val data: ChannelInfo

    init {
        extractor.fetchPage()
        data = ChannelInfo(
            name = extractor.name,
            subscriberCount = extractor.subscriberCount,
            description = extractor.description,
            thumbnails = extractor.avatars.first().url,
            verified = extractor.isVerified,
            banner = extractor.banners.first().url,
            id = extractor.id,
            tabs = extractor.tabs.map {
                ChannelTab(
                    name = it.url.split("/").last(),
                    url = Channel_Base_Url + it.url,
                )
            },
        )
        tabExtractors = buildList {
            data.tabs.forEach {
                try {
                    add(YtService.getChannelTabExtractor(YtService.channelTabLHFactory.fromUrl(it.url)))
                } catch (e: Exception) {
                }
            }
        }
    }

    fun fetchTab(tab: ChannelTab) {
        val tabExtractor = tabExtractors.first { it.url == tab.url }
        tabExtractor.fetchPage()
        // todo return result
    }
}
