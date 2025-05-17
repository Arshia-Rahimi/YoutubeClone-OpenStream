package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.model.extractordata.OfflineFirstPlaylist
import com.github.openstream.core.model.extractordata.OnlinePlaylist
import com.github.openstream.core.model.extractordata.PlaylistItem
import com.github.openstream.core.model.extractordata.VideoItem
import com.github.openstream.core.model.extractordata.YoutubePlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PlaylistRepository {
    val playlists: SharedFlow<List<PlaylistItem>>

    // local playlists
    fun createPlaylist(playlistName: String): Flow<Resource<Success>>

    fun deletePlaylist(playlist: PlaylistItem.LocalPlaylistItem): Flow<Resource<Success>>

    fun addToPlaylist(
        videos: List<VideoItem>,
        playlist: PlaylistItem.LocalPlaylistItem,
    ): Flow<Resource<Success>>
    
    fun removeFromPlaylist(
        videos: List<VideoItem>,
        playlist: PlaylistItem.LocalPlaylistItem,
    ): Flow<Resource<Success>>

    fun saveVideoToPlaylists(
        video: VideoItem,
        playlistsMap: Map<PlaylistItem.LocalOnlyPlaylistItem, Boolean>
    ): Flow<Resource<Success>>
    
    fun getPlaylistSavedVideos(playlist: PlaylistItem.LocalPlaylistItem): Flow<List<VideoItem>>
    
    // youtube playlists
    fun getPlaylist(playlist: PlaylistItem.YoutubePlaylistItem): Flow<Resource<YoutubePlaylist>>

    // offline first playlists
    fun getPlaylistFirstPage(playlist: OfflineFirstPlaylist): Flow<Resource<Success>>

    fun getNextPage(currentPlaylist: OfflineFirstPlaylist): Flow<Resource<Success>>

    // online playlists
    fun savePlaylist(playlist: PlaylistItem.OnlinePlaylistItem): Flow<Resource<Success>>

    fun getPlaylistFirstPage(playlist: OnlinePlaylist): Flow<Resource<List<VideoItem>>>

    fun getNextPage(currentPlaylist: OnlinePlaylist): Flow<Resource<List<VideoItem>>>

}
