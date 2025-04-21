package com.github.openstream.core.extractor

import androidx.compose.runtime.toMutableStateList
import com.github.openstream.core.extractor.util.YtService
import com.github.openstream.core.model.extractordata.toList
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import com.github.openstream.core.model.playlist.OnlinePlaylist
import com.github.openstream.core.model.playlist.YoutubePlaylist
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
        val items = extractor.initialPage.items.toList()
        val nextPage = extractor.initialPage.nextPage
        return OnlinePlaylist(
            extractor = extractor,
            items = items.toMutableStateList(),
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
            playlist.items.addAll(currentPage.items.toList())
        }
    }
    
}
