package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.LocalPlaylist
import com.github.openstream.core.model.OfflineFirstPlaylist
import com.github.openstream.core.model.OnlinePlaylist
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.YoutubePlaylist
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PlaylistRepository {
    val playlists: SharedFlow<List<DataItem.Playlist>>
    
    fun createPlaylist(playlistName: String): Flow<Resource<Success>>

    fun deletePlaylist(playlist: DataItem.Playlist.LocalPlaylist): Flow<Resource<Success>>

    fun deletePlaylist(playlist: DataItem.Playlist.OfflineFirstPlaylist): Flow<Resource<Success>>

    fun deletePlaylist(playlist: LocalPlaylist): Flow<Resource<Success>>

    fun addToPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Long,
    ): Flow<Resource<Success>>

    fun removeFromPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Long,
    ): Flow<Resource<Success>>
    
    fun addToWatchLater(videos: List<DataItem.Video>): Flow<Resource<Success>>
    
    fun removeFromWatchLater(videos: List<DataItem.Video>): Flow<Resource<Success>>
    
    fun getPlaylist(playlist: DataItem.Playlist): Flow<Resource<Playlist>>

    fun getNextPage(currentPlaylist: YoutubePlaylist): Flow<Resource<Success>>

    fun savePlaylist(playlist: OnlinePlaylist): Flow<Resource<Success>>

    fun savePlaylist(playlist: DataItem.Playlist.OnlinePlaylist): Flow<Resource<Success>>

    fun syncPlaylist(playlist: OfflineFirstPlaylist): Flow<Resource<Playlist>>

    fun saveVideoToPlaylists(
        video: DataItem.Video,
        playlistsMap: Map<DataItem.Playlist.LocalPlaylist, Boolean>
    ): Flow<Resource<Success>>
}
