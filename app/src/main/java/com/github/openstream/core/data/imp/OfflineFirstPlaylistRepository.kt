package com.github.openstream.core.data.imp

import androidx.compose.runtime.mutableStateListOf
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.extractor.PlaylistExtractor
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import com.github.openstream.core.model.playlist.LocalPlaylist
import com.github.openstream.core.model.playlist.Playlist
import com.github.openstream.core.model.playlist.YoutubePlaylist
import com.github.openstream.core.shared.exceptions.LocalPlaylistNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OfflineFirstPlaylistRepository(
    private val db: OpenStreamDatabase,
) : PlaylistRepository {
    
    override val localPlaylists = db.playlistDao().index().map {
        it.map {
            // todo get count from videos table
            DataItem.Playlist(
                name = it.name,
                url = it.url,
                thumbnail = it.thumbnail,
                channelUrl = it.channelUrl,
                channelName = it.channelName,
                count = it.count ?: 0L,
                channelVerified = it.isChannelVerified,
            )
        }
    }
    
    override suspend fun createPlaylist(playlistName: String): Flow<Resource<Success>> =
        flow {
            if (db.playlistDao().index().first().any { it.name == playlistName }) {
                throw Exception("a playlist with the name provided exists")
            }
            
            db.playlistDao().upsert(PlaylistEntity(name = playlistName))
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun deletePlaylist(playlist: DataItem.Playlist): Flow<Resource<Success>> =
        flow {
            db.playlistDao().delete(playlist.toEntity(null) as PlaylistEntity)
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun addToPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int,
    ): Flow<Resource<Success>> =
        flow {
            // todo
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun removeFromPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int,
    ): Flow<Resource<Success>> = flow {
        // todo
        emit(Success)
    }.asResult(Dispatchers.IO)
    
    override suspend fun getPlaylist(id: Int): Flow<Resource<Playlist>> =
        flow {
            // todo add videos to db and get count and items from
            db.playlistDao().search(id)?.let {
                LocalPlaylist(
                    id = it.id,
                    items = mutableStateListOf(),
                    metadata = PlaylistMetadata(
                        name = it.name,
                        channelUrl = it.channelUrl,
                        isChannelVerified = it.isChannelVerified,
                        count = it.count ?: 0L,
                        channelName = it.channelName,
                    )
                )
            }?.let { emit(it) } ?: throw LocalPlaylistNotFoundException()
        }.asResult(Dispatchers.IO)
    
    override suspend fun getPlaylist(url: String): Flow<Resource<Playlist>> =
        flow {
            emit(PlaylistExtractor.fetchPlaylist(url))
        }.asResult(Dispatchers.IO)
    
    override suspend fun getNextPage(currentPlaylist: YoutubePlaylist): Flow<Resource<Success>> =
        flow {
            PlaylistExtractor.fetchNextPage(currentPlaylist)
            emit(Success)
        }.asResult(Dispatchers.IO)
}
