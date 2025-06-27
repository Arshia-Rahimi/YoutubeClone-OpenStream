package com.github.openstream.core.extractor.datasource

import com.github.openstream.core.extractor.YtService
import com.github.openstream.core.model.dataitem.PlaylistItem
import com.github.openstream.core.model.dataitem.VideoItem
import com.github.openstream.core.model.dataitem.toListOfVideos
import com.github.openstream.core.model.extractor.OnlinePlaylistExtractor
import com.github.openstream.core.model.extractor.PlaylistExtractor
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

object PlaylistRemoteDataSource {
    fun fetchPlaylist(url: String): PlaylistItem.OnlinePlaylistItem {
        val extractor = YtService.getPlaylistExtractor(url)
        extractor.fetchPage()
        return PlaylistItem.OnlinePlaylistItem(
            name = extractor.name,
            thumbnail = extractor.thumbnails.first().url,
            count = extractor.streamCount,
            channelName = extractor.uploaderName,
            channelUrl = extractor.uploaderUrl,
            isChannelVerified = extractor.isUploaderVerified,
            url = extractor.url,
        )
    }
    
    fun fetchPlaylist(playlist: PlaylistItem.YoutubePlaylistItem): OnlinePlaylistExtractor {
        val extractor =
            YtService.getPlaylistExtractor(playlist.url) as YoutubePlaylistExtractor
        extractor.fetchPage()
        return OnlinePlaylistExtractor(
            extractor = extractor,
            data = playlist,
        )
    }
    
    fun fetchFirstPage(playlist: PlaylistExtractor): List<VideoItem> {
        playlist.nextPage = playlist.extractor?.initialPage?.nextPage
        return playlist.extractor?.initialPage?.items?.toListOfVideos() ?: emptyList()
    }
    
    fun fetchNextPage(playlist: PlaylistExtractor): List<VideoItem> {
        requireNotNull(playlist.extractor) { "fetch the playlist first to sync it with youtube" }
        val currentPage = playlist.extractor!!.getPage(playlist.nextPage)
        playlist.nextPage = currentPage.nextPage
        return currentPage.items.toListOfVideos()
    }
}
