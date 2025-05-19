package com.github.arshiarahimi.openstream.core.data

import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.Success
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.core.model.extractor.OfflineFirstPlaylistExtractor
import com.github.arshiarahimi.openstream.core.model.extractor.OnlinePlaylistExtractor
import com.github.arshiarahimi.openstream.core.model.extractor.PlaylistExtractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PlaylistRepository {
    val playlists: SharedFlow<List<PlaylistItem>>

    // local playlists
    fun createPlaylist(playlistName: String): Flow<Resource<Success>>

    fun deletePlaylist(playlist: PlaylistItem.LocalPlaylistItem): Flow<Resource<Success>>

    fun addToPlaylist(
        videos: List<VideoItem>,
        playlistId: Long,
    ): Flow<Resource<Success>>

    fun removeFromPlaylist(
        videos: List<VideoItem>,
        playlistId: Long,
    ): Flow<Resource<Success>>

    fun saveVideoToPlaylists(
        video: VideoItem,
        playlistsMap: Map<PlaylistItem.LocalOnlyPlaylistItem, Boolean>
    ): Flow<Resource<Success>>
    
    fun getPlaylistSavedVideos(playlist: PlaylistItem.LocalPlaylistItem): Flow<List<VideoItem>?>

    // youtube playlists
    fun getPlaylist(playlist: PlaylistItem.YoutubePlaylistItem): Flow<Resource<PlaylistExtractor>>

    // offline first playlists
    fun getPlaylistFirstPage(playlist: OfflineFirstPlaylistExtractor): Flow<Resource<Success>>

    fun getNextPage(currentPlaylist: OfflineFirstPlaylistExtractor): Flow<Resource<Success>>

    // online playlists
    fun savePlaylist(playlist: PlaylistItem.OnlinePlaylistItem): Flow<Resource<Success>>

    fun getPlaylistFirstPage(playlist: OnlinePlaylistExtractor): Flow<Resource<List<VideoItem>>>

    fun getNextPage(currentPlaylist: OnlinePlaylistExtractor): Flow<Resource<List<VideoItem>>>

}
