package com.github.freetube.core.data

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.Success
import com.github.freetube.core.extractor.model.DataItem
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    val playlists: Flow<List<DataItem>>

    suspend fun createPlaylist(playlistName: String): Flow<Resource<Success>>

    suspend fun deletePlaylist(playlist: DataItem.Playlist): Flow<Resource<Success>>

    suspend fun addToPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int
    ): Flow<Resource<Success>>

    suspend fun removeFromPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int
    ): Flow<Resource<Success>>
}
