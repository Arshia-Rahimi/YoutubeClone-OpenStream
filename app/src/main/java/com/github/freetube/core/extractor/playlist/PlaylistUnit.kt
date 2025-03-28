package com.github.freetube.core.extractor.playlist

import com.github.freetube.core.extractor.YtService
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.model.toList
import org.schabi.newpipe.extractor.Page

@Suppress("UNCHECKED_CAST")
class PlaylistUnit(
    private val url: String,
) {
    private val extractor = YtService.getPlaylistExtractor(url)
    var nextPage: Page?
    val firstPage: PlaylistResult

    init {
        extractor.fetchPage()
        firstPage = PlaylistResult(
            name = extractor.name,
            items = extractor.initialPage.items.toList() as List<DataItem.Video>,
            channelName = extractor.uploaderName,
            channelUrl = extractor.uploaderUrl,
            isChannelVerified = extractor.isUploaderVerified,
            description = extractor.description.content,
        )
        nextPage = extractor.initialPage.nextPage
    }

    fun fetchNextPage(): List<DataItem.Video>? =
        nextPage?.let {
            val currentPage = extractor.getPage(nextPage)
            nextPage = currentPage.nextPage
            return currentPage.items.toList() as List<DataItem.Video>
        }
}
