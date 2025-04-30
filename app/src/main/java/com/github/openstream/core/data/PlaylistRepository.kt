package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    val localPlaylists: Flow<List<DataItem>>

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

    fun getPlaylist(playlist: DataItem.Playlist): Flow<Resource<Playlist>>

    suspend fun getNextPage(currentPlaylist: Playlist): Flow<Resource<Success>>

    suspend fun savePlaylist(playlist: Playlist): Flow<Resource<Success>>

    suspend fun syncPlaylist(playlist: Playlist): Flow<Resource<Playlist>>
}
