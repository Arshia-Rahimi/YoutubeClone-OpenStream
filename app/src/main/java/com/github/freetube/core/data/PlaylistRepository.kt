package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.playlist.PlaylistUnit
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getPlaylist(url: String): Flow<Resource<PlaylistUnit>>

    suspend fun getNextPage(currentPlaylist: PlaylistUnit): Flow<Resource<List<DataItem.Video>?>>
}
