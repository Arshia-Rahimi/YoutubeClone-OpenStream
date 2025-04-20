package com.github.openstream.core.extractor.playlist

import com.github.openstream.core.extractor.YtService
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.model.toList
import org.schabi.newpipe.extractor.Page

class PlaylistUnit(
    url: String,
) {
    private val extractor = YtService.getPlaylistExtractor(url)
    var nextPage: Page?
    val firstPage: PlaylistMetadata

    init {
        extractor.fetchPage()
        firstPage = PlaylistMetadata(
            name = extractor.name,
            channelName = extractor.uploaderName,
            channelUrl = extractor.uploaderUrl,
            isChannelVerified = extractor.isUploaderVerified,
            description = extractor.description.content,
            count = extractor.streamCount,
        )
        nextPage = extractor.initialPage.nextPage
    }

    fun fetchNextPage(): List<DataItem>? =
        nextPage?.let {
            val currentPage = extractor.getPage(nextPage)
            nextPage = currentPage.nextPage
            return currentPage.items.toList()
        }
}
