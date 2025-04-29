package com.github.openstream.core.extractor

import com.github.openstream.core.extractor.util.YtService
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import com.github.openstream.core.model.extractordata.toListOfDataItem
import com.github.openstream.core.model.extractordata.toMutableStateListOfDataItem
import com.github.openstream.core.model.OnlinePlaylist
import com.github.openstream.core.model.YoutubePlaylist
import com.github.openstream.core.shared.exceptions.OfflineFirstPlaylistNotFetchedException
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

object PlaylistExtractor {
    fun fetchPlaylist(url: String): OnlinePlaylist {
        val extractor = YtService.getPlaylistExtractor(url) as YoutubePlaylistExtractor
        extractor.fetchPage()
        val metadata = PlaylistMetadata(
            name = extractor.name,
            channelName = extractor.uploaderName,
            channelUrl = extractor.uploaderUrl,
            isChannelVerified = extractor.isUploaderVerified,
            count = extractor.streamCount,
        )
        val items = extractor.initialPage.items.toMutableStateListOfDataItem()
        val nextPage = extractor.initialPage.nextPage
        return OnlinePlaylist(
            extractor = extractor,
            items = items,
            metadata = metadata,
            nextPage = nextPage,
            url = url,
        )
    }
    
    fun fetchNextPage(playlist: YoutubePlaylist) {
        if(playlist.extractor == null) throw OfflineFirstPlaylistNotFetchedException()
        playlist.nextPage?.let {
            val currentPage = playlist.extractor!!.getPage(it)
            playlist.nextPage = currentPage.nextPage
            playlist.items.addAll(currentPage.items.toListOfDataItem())
        }
    }
    
}
