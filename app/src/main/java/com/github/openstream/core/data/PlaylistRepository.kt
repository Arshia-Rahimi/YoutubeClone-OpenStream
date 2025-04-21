package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.playlist.Playlist
import com.github.openstream.core.model.playlist.YoutubePlaylist
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
    
    suspend fun getPlaylist(id: Int): Flow<Resource<Playlist>>
    
    suspend fun getPlaylist(url: String): Flow<Resource<Playlist>>

    suspend fun getNextPage(currentPlaylist: YoutubePlaylist): Flow<Resource<Success>>
}
