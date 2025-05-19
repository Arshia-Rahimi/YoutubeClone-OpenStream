package com.github.arshiarahimi.openstream.core.extractor.datasource

import com.github.arshiarahimi.openstream.core.extractor.YtService
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.model.dataitem.toListOfVideos
import com.github.arshiarahimi.openstream.core.model.extractor.OnlinePlaylistExtractor
import com.github.arshiarahimi.openstream.core.model.extractor.PlaylistExtractor
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

object PlaylistRemoteDataSource {
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
