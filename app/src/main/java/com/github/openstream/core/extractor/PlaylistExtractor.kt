package com.github.openstream.core.extractor

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.buildImmutableArray
import com.github.openstream.core.extractor.util.YtService
import com.github.openstream.core.model.OfflineFirstPlaylist
import com.github.openstream.core.model.OnlinePlaylist
import com.github.openstream.core.model.YoutubePlaylist
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import com.github.openstream.core.model.extractordata.toImmutableArrayOfDataItem
import com.github.openstream.core.shared.exceptions.OfflineFirstPlaylistNotFetchedException
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor

@Suppress("UNCHECKED_CAST")
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
        val items =
            extractor.initialPage.items.toImmutableArrayOfDataItem() as ImmutableArray<DataItem.Video>
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
        if (playlist.extractor == null) throw OfflineFirstPlaylistNotFetchedException()
        if (playlist.nextPage == null) return
        val currentPage = playlist.extractor!!.getPage(playlist.nextPage)
        playlist.nextPage = currentPage.nextPage

        playlist.items =
            buildImmutableArray {
                when (playlist) {
                    is OnlinePlaylist -> {
                        addAll(playlist.items)
                        addAll(currentPage.items.toImmutableArrayOfDataItem() as ImmutableArray<DataItem.Video>)
                    }

                    is OfflineFirstPlaylist -> {
                        // todo sort items
                        val newItems =
                            currentPage.items.toImmutableArrayOfDataItem() as ImmutableArray<DataItem.Video>
                        addAll(newItems)
                        playlist.items.forEach { previousItem ->
                            if (newItems.none { it.url == previousItem.url }) {
                                add(previousItem)
                            }
                        }
                    }
                }
            }

    }
}
