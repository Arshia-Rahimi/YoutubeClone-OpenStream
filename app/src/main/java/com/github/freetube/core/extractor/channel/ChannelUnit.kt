package com.github.freetube.core.extractor.channel

import com.github.freetube.core.common.youtubeService
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor

class ChannelUnit(
    val url: String,
) {
    private val extractor = youtubeService.getChannelExtractor(url)
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
            tabs = extractor.tabs.map { it.url },
            id = extractor.id,
        )
        println(extractor.id + "--" + extractor.tabs.first().id)
        tabExtractors = buildList {
            data.tabs.forEach {
                add(youtubeService.getChannelTabExtractorFromId(data.id, it))
            }
        }
    }

}
