package com.github.openstream.core.data.imp

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.extractor.PlaylistExtractor
import com.github.openstream.core.model.OfflineFirstPlaylist
import com.github.openstream.core.model.OnlinePlaylist
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.YoutubePlaylist
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.toOfflineFirstPlaylist
import com.github.openstream.core.shared.exceptions.LocalPlaylistNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OfflineFirstPlaylistRepository(
    private val db: OpenStreamDatabase,
) : PlaylistRepository {
    
    override val localPlaylists = db.playlistDao().index()
        .map { it.map { playlist -> playlist.toDataItem() } }
    
    override suspend fun createPlaylist(playlistName: String): Flow<Resource<Success>> = flow {
        if (db.playlistDao().index().first()
                .any { it.name == playlistName }
        ) throw Exception("a playlist with the name provided exists")
        
        db.playlistDao().upsert(PlaylistEntity(name = playlistName, count = 0L))
        emit(Success)
    }.asResult(Dispatchers.IO)
    
    override suspend fun deletePlaylist(playlist: DataItem.Playlist): Flow<Resource<Success>> =
        flow {
            db.playlistDao().delete(playlist.toEntity())
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun addToPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int,
    ): Flow<Resource<Success>> = flow {
        db.videoDao().upsert(*videos.map { it.toEntity() }.toTypedArray())
        db.playlistDao().incrementPlaylistCount(playlistId)
        emit(Success)
    }.asResult(Dispatchers.IO)
    
    override suspend fun removeFromPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int,
    ): Flow<Resource<Success>> = flow {
        db.videoDao().delete(*videos.map { it.toEntity() }.toTypedArray())
        db.playlistDao().decrementPlaylistCount(playlistId)
        emit(Success)
    }.asResult(Dispatchers.IO)
    
    override suspend fun getNextPage(currentPlaylist: YoutubePlaylist): Flow<Resource<Success>> =
        flow {
            PlaylistExtractor.fetchNextPage(currentPlaylist)
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun savePlaylist(playlist: OnlinePlaylist): Flow<Resource<Success>> = flow {
        // todo check before adding
        emit(Success)
    }.asResult(Dispatchers.IO)
    
    override suspend fun getPlaylist(playlist: DataItem.Playlist): Flow<Resource<Playlist>> =
        flow<Playlist> {
            when (playlist) {
                is DataItem.Playlist.LocalPlaylist -> {
                    db.playlistDao().getPlaylistWithVideos(playlist.id)?.let {
                        val correctedCount: Long = it.videos.size.toLong()
                        if (it.playlist.count != correctedCount) {
                            db.playlistDao().upsert(it.playlist.copy(count = correctedCount))
                        }
                        emit(it.toPlaylistObject())
                    } ?: throw LocalPlaylistNotFoundException()
                }
                
                is DataItem.Playlist.OnlinePlaylist -> {
                    emit(PlaylistExtractor.fetchPlaylist(playlist.url))
                }
                
                is DataItem.Playlist.OfflineFirstPlaylist -> {
                    coroutineScope {
                        // todo handle error
                        val localData = db.playlistDao().getPlaylistWithVideos(playlist.id)?.let {
                            val correctedCount: Long = it.videos.size.toLong()
                            if (it.playlist.count != correctedCount) {
                                db.playlistDao()
                                    .upsert(it.playlist.copy(count = correctedCount))
                            }
                            it.toPlaylistObject()
                        }?.let {
                            emit(it)
                            return@coroutineScope
                        }
                    }
                }
            }
        }.asResult(Dispatchers.IO)
    
    override suspend fun syncPlaylist(playlist: OfflineFirstPlaylist): Flow<Resource<OfflineFirstPlaylist>> =
        flow {
            coroutineScope {
                val updatedPlaylist = PlaylistExtractor.fetchPlaylist(playlist.url)
                    .toOfflineFirstPlaylist(playlist.id)
                
                val updatePlaylistDeferred =
                    async { db.playlistDao().upsert(updatedPlaylist.toEntity()) }
                val updateVideosDeferred = async {
                    db.videoDao()
                        .upsert(*updatedPlaylist.items.map { it.toEntity() }.toTypedArray())
                }
                
                updatePlaylistDeferred.await()
                updateVideosDeferred.await()
                
                emit(updatedPlaylist)
            }
        }.asResult(Dispatchers.IO)
    
}
