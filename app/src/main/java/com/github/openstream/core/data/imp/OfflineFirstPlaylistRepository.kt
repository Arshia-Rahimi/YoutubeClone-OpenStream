package com.github.openstream.core.data.imp

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.PlaylistVideoCrossRef
import com.github.openstream.core.extractor.PlaylistExtractor
import com.github.openstream.core.model.LocalPlaylist
import com.github.openstream.core.model.OfflineFirstPlaylist
import com.github.openstream.core.model.OnlinePlaylist
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.YoutubePlaylist
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.toOfflineFirstPlaylist
import com.github.openstream.core.shared.WATCH_LATER_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class OfflineFirstPlaylistRepository(
    private val db: OpenStreamDatabase,
    private val scope: CoroutineScope,
) : PlaylistRepository {
    
    override val playlists = db.playlistDao().indexFlow()
        .map { it.map { playlist -> playlist.toDataItem() } }
        .shareIn(
            scope = scope,
            started = SharingStarted.Lazily,
            replay = 1,
        )
    
    override fun createPlaylist(playlistName: String): Flow<Resource<Success>> = flow {
        db.playlistDao().insert(PlaylistEntity(name = playlistName, count = 0L))
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun deletePlaylist(playlist: DataItem.Playlist.OfflineFirstPlaylist): Flow<Resource<Success>> =
        flow {
            db.playlistDao().delete(playlist.toEntity())
            emit(Success)
        }.asResult(Dispatchers.IO)

    override fun deletePlaylist(playlist: DataItem.Playlist.LocalPlaylist): Flow<Resource<Success>> =
        flow {
            require(playlist.id != WATCH_LATER_ID)
            db.playlistDao().delete(playlist.toEntity())
            emit(Success)
        }.asResult(Dispatchers.IO)

    override fun deletePlaylist(playlist: LocalPlaylist): Flow<Resource<Success>> =
        flow {
            require(playlist.id != WATCH_LATER_ID)
            db.playlistDao().delete(playlist.toEntity())
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override fun addToPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Long,
    ): Flow<Resource<Success>> = flow {
        val playlist = db.playlistDao().get(playlistId)
        if (playlist == null) throw Exception("playlist doesn't exist")
        if (playlist.url != null) throw Exception("playlist isn't local")
        
        val ids = db.videoDao().insert(*videos.map { it.toEntity() }.toTypedArray())
        db.m2mDao()
            .addToPlaylist(*ids.map { PlaylistVideoCrossRef(playlistId, it) }.toTypedArray())
        emit(Success)
    }.asResult(Dispatchers.IO)
    
    override fun removeFromPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Long,
    ): Flow<Resource<Success>> = flow {
        db.videoDao().delete(*videos.map { it.toEntity() }.toTypedArray())
        emit(Success)
    }.asResult(Dispatchers.IO)
    
    override fun getNextPage(currentPlaylist: YoutubePlaylist): Flow<Resource<Success>> =
        flow {
            PlaylistExtractor.fetchNextPage(currentPlaylist)
            emit(Success)
        }.asResult(Dispatchers.IO)

    override fun savePlaylist(playlist: OnlinePlaylist): Flow<Resource<Success>> = flow {
        val playlistId = db.playlistDao().insert(playlist.toEntity()).first()
        val videoIds = db.videoDao().insert(
            *playlist.items
                .map { it.toEntity() }
                .toTypedArray()
        )
        
        db.m2mDao().addToPlaylist(
            *videoIds
                .map { PlaylistVideoCrossRef(playlistId, it) }
                .toTypedArray()
        )
        
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun savePlaylist(playlist: DataItem.Playlist.OnlinePlaylist): Flow<Resource<Success>> =
        flow {
            if (db.playlistDao().index().any { it.url == playlist.url })
                throw Exception("This playlist already exist in your library")
            
            db.playlistDao().insert(playlist.toEntity())
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override fun getPlaylist(playlist: DataItem.Playlist): Flow<Resource<Playlist>> =
        flow<Playlist> {
            when (playlist) {
                is DataItem.Playlist.LocalPlaylist -> {
                    db.m2mDao().getPlaylistWithVideos(playlist.id)?.let {
                        val updatedPlaylist = it.copy(playlist = it.playlist.copy(count = it.videos.size.toLong()))
                        db.playlistDao().upsert(updatedPlaylist.playlist)
                        emit(updatedPlaylist.toPlaylistObject())
                    } ?: throw Exception("couldn't find playlist")
                }
                
                is DataItem.Playlist.OnlinePlaylist -> {
                    emit(PlaylistExtractor.fetchPlaylist(playlist.url))
                }
                
                is DataItem.Playlist.OfflineFirstPlaylist -> {
                    // todo handle error
                    val localData = db.m2mDao().getPlaylistWithVideos(playlist.id)?.let {
                        val correctedCount: Long = it.videos.size.toLong()
                        if (it.playlist.count != correctedCount) {
                            db.playlistDao()
                                .upsert(it.playlist.copy(count = correctedCount))
                        }
                        it.toPlaylistObject()
                    }?.let {
                        emit(it)
                    }
                }
            }
        }.asResult(Dispatchers.IO)

    override fun syncPlaylist(playlist: OfflineFirstPlaylist): Flow<Resource<Playlist>> =
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
                
                db.m2mDao().getPlaylistWithVideos(playlist.id)?.toPlaylistObject().let {
                    emit(it ?: playlist)
                }
            }
        }.asResult(Dispatchers.IO)

    override fun saveVideoToPlaylists(
        video: DataItem.Video,
        playlistsMap: Map<DataItem.Playlist.LocalPlaylist, Boolean>
    ): Flow<Resource<Success>> = flow {
        val videoId = video.id ?: db.videoDao().insert(video.toEntity()).first()
        val currentPlaylists =
            db.m2mDao().getVideoWithPlaylists(videoId)?.playlists?.map { it.playlistId }
                ?: emptyList()
        
        coroutineScope {
            playlistsMap.map { (playlist, isInPlaylist) ->
                async {
                    when {
                        isInPlaylist && playlist.id !in currentPlaylists ->
                            db.m2mDao().addToPlaylist(PlaylistVideoCrossRef(playlist.id, videoId))
                        
                        !isInPlaylist && playlist.id in currentPlaylists ->
                            db.m2mDao()
                                .removeFromPlaylist(PlaylistVideoCrossRef(playlist.id, videoId))
                    }
                }
            }.awaitAll()
            emit(Success)
        }
    }.asResult(Dispatchers.IO)
    
}
