package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    val localPlaylists: Flow<List<DataItem.Playlist>>

    fun createPlaylist(playlistName: String): Flow<Resource<Success>>

    fun deletePlaylist(playlist: DataItem.Playlist): Flow<Resource<Success>>
    
    fun deletePlaylist(playlist: Playlist): Flow<Resource<Success>>

    fun addToPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Long
    ): Flow<Resource<Success>>

    fun removeFromPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Long
    ): Flow<Resource<Success>>
    
    fun addToWatchLater(videos: List<DataItem.Video>): Flow<Resource<Success>>
    
    fun removeFromWatchLater(videos: List<DataItem.Video>): Flow<Resource<Success>>
    
    fun getPlaylist(playlist: DataItem.Playlist): Flow<Resource<Playlist>>

    fun getNextPage(currentPlaylist: Playlist): Flow<Resource<Success>>

    fun savePlaylist(playlist: Playlist): Flow<Resource<Success>>
    
    fun savePlaylist(playlist: DataItem.Playlist): Flow<Resource<Success>>

    fun syncPlaylist(playlist: Playlist): Flow<Resource<Playlist>>
}
