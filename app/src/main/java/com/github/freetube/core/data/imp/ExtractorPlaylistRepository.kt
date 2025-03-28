package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.PlaylistRepository
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.playlist.PlaylistUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExtractorPlaylistRepository : PlaylistRepository {
    override suspend fun getPlaylist(url: String): Flow<Resource<PlaylistUnit>> =
        flow { emit(PlaylistUnit(url)) }
            .asResult(Dispatchers.IO)

    override suspend fun getNextPage(currentPlaylist: PlaylistUnit): Flow<Resource<List<DataItem.Video>?>> =
        flow { emit(currentPlaylist.fetchNextPage()) }
            .asResult(Dispatchers.IO)
}
