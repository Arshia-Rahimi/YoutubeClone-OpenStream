package com.github.freetube.core.extractor.channel

import com.github.freetube.core.extractor.YtService
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

class ChannelUnit(
    val url: String,
) {
    private val extractor = YtService.getChannelExtractor(url)
    private val tabExtractors: List<ChannelTabExtractor>
    val data: ChannelData

    init {
        extractor.fetchPage()
        data = ChannelData(
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
                    url = it.url,
                )
            },
        )
        tabExtractors = buildList {
            data.tabs.forEach {
                add(YtService.getChannelTabExtractorFromId(data.id, it.url))
            }
        }
    }

    fun fetchTab(tab: ChannelTab) {
        val tabExtractor = tabExtractors.first { it.url == tab.url }
        tabExtractor.fetchPage()
        // todo return result
    }
}
