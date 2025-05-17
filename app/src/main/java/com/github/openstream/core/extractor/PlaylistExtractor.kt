package com.github.openstream.core.extractor

import com.github.openstream.core.extractor.util.YtService
import com.github.openstream.core.model.extractordata.OnlinePlaylist
import com.github.openstream.core.model.extractordata.PlaylistItem
import com.github.openstream.core.model.extractordata.VideoItem
import com.github.openstream.core.model.extractordata.YoutubePlaylist
import com.github.openstream.core.model.extractordata.toListOfVideos
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

object PlaylistExtractor {
    fun fetchPlaylist(playlist: PlaylistItem.YoutubePlaylistItem): OnlinePlaylist {
        val extractor = YtService.getPlaylistExtractor(playlist.url) as YoutubePlaylistExtractor
        extractor.fetchPage()
        return OnlinePlaylist(
            extractor = extractor,
            data = playlist,
        )
    }

    fun fetchFirstPage(playlist: YoutubePlaylist): List<VideoItem> {
        playlist.nextPage = playlist.extractor?.initialPage?.nextPage
        return playlist.extractor?.initialPage?.items?.toListOfVideos() ?: emptyList()
    }

    fun fetchNextPage(playlist: YoutubePlaylist): List<VideoItem> {
        requireNotNull(playlist.extractor) { "fetch the playlist first to sync it with youtube" }
        val currentPage = playlist.extractor!!.getPage(playlist.nextPage)
        playlist.nextPage = currentPage.nextPage
        return currentPage.items.toListOfVideos()
    }
}
