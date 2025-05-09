package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.LocalOnlyPlaylist
import com.github.openstream.core.model.OfflineFirstPlaylist
import com.github.openstream.core.model.OnlinePlaylist
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.YoutubePlaylist
import com.github.openstream.core.model.extractordata.PlaylistItem
import com.github.openstream.core.model.extractordata.VideoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PlaylistRepository {
    val playlists: SharedFlow<List<PlaylistItem>>
    
    fun createPlaylist(playlistName: String): Flow<Resource<Success>>

    fun deletePlaylist(playlist: PlaylistItem.LocalPlaylistItem): Flow<Resource<Success>>

    fun deletePlaylist(playlist: LocalOnlyPlaylist): Flow<Resource<Success>>

    fun addToPlaylist(
        videos: List<VideoItem>,
        playlistId: Long,
    ): Flow<Resource<Success>>

    fun removeFromPlaylist(
        videos: List<VideoItem>,
        playlistId: Long,
    ): Flow<Resource<Success>>

    fun getPlaylist(playlist: PlaylistItem): Flow<Resource<Playlist>>

    fun getNextPage(currentPlaylist: YoutubePlaylist): Flow<Resource<Success>>

    fun savePlaylist(playlist: OnlinePlaylist): Flow<Resource<Success>>

    fun savePlaylist(playlist: PlaylistItem.OnlinePlaylistItem): Flow<Resource<Success>>

    fun syncPlaylist(playlist: OfflineFirstPlaylist): Flow<Resource<Playlist>>

    fun saveVideoToPlaylists(
        video: VideoItem,
        playlistsMap: Map<PlaylistItem.LocalOnlyPlaylistItem, Boolean>
    ): Flow<Resource<Success>>
}
