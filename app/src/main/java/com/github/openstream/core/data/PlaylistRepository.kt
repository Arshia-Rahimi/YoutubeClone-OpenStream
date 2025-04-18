package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.playlist.PlaylistUnit
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
    
    suspend fun getPlaylist(url: String): Flow<Resource<PlaylistUnit>>

    suspend fun getNextPage(currentPlaylist: PlaylistUnit): Flow<Resource<List<DataItem>?>>
}
