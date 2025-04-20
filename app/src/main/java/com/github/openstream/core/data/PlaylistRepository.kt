package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.playlist.PlaylistUnit
import com.github.openstream.core.model.playlist.Playlist
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
    
    suspend fun getPlaylist(id: String): Flow<Resource<Playlist>>

    suspend fun getNextPage(currentPlaylist: Playlist): Flow<Resource<Success>>
}
