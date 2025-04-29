package com.github.openstream.core.data.imp

import androidx.compose.runtime.toMutableStateList
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.VideoEntity
import com.github.openstream.core.extractor.PlaylistExtractor
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.PlaylistMetadata
import com.github.openstream.core.model.LocalPlaylist
import com.github.openstream.core.model.OfflineFirstPlaylist
import com.github.openstream.core.model.OnlinePlaylist
import com.github.openstream.core.model.Playlist
import com.github.openstream.core.model.YoutubePlaylist
import com.github.openstream.core.model.toOfflineFirstPlaylist
import com.github.openstream.core.shared.exceptions.LocalPlaylistNotFoundException
import com.github.openstream.ui.global.navigation.Tabs
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
            if(it.url == null) {
                DataItem.Playlist.LocalPlaylist(
                    name = it.name,
                    thumbnail = it.thumbnail,
                    count = it.count,
                    id = it.id,
                )
            } else {
                DataItem.Playlist.OfflineFirstPlaylist(
                    name = it.name,
                    url = it.url,
                    thumbnail = it.thumbnail,
                    channelUrl = it.channelUrl ?: "",
                    channelName = it.channelName ?: "",
                    count = it.count,
                    isChannelVerified = it.isChannelVerified == true,
                    id = it.id,
                )
            }
        }
    }
    
    override suspend fun createPlaylist(playlistName: String): Flow<Resource<Success>> =
        flow {
            if (db.playlistDao().index().first().any { it.name == playlistName })
                throw Exception("a playlist with the name provided exists")
            
            db.playlistDao().upsert(PlaylistEntity(name = playlistName, count = 0L))
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun deletePlaylist(playlist: DataItem.Playlist): Flow<Resource<Success>> =
        flow {
            db.playlistDao().delete(playlist.toEntity() as PlaylistEntity)
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun addToPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int,
    ): Flow<Resource<Success>> =
        flow {
            db.videoDao().upsert(*videos.map { it.toEntity() as VideoEntity }.toTypedArray())
            db.playlistDao().incrementPlaylistCount(playlistId)
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun removeFromPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int,
    ): Flow<Resource<Success>> = flow {
        db.videoDao().delete(*videos.map { it.toEntity() as VideoEntity }.toTypedArray())
        db.playlistDao().decrementPlaylistCount(playlistId)
        emit(Success)
    }.asResult(Dispatchers.IO)
    
    override suspend fun getNextPage(currentPlaylist: YoutubePlaylist): Flow<Resource<Success>> =
        flow {
            PlaylistExtractor.fetchNextPage(currentPlaylist)
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun savePlaylist(playlist: OnlinePlaylist): Flow<Resource<Success>> =
        flow {
            // todo check before adding
            emit(Success)
        }.asResult(Dispatchers.IO)
    
    override suspend fun getPlaylist(playlist: DataItem.Playlist): Flow<Resource<Playlist>> =
        flow<Playlist> {
            when (playlist) {
                is DataItem.Playlist.LocalPlaylist -> {
                    db.playlistDao().getPlaylistWithVideos(playlist.id)?.let {
                        LocalPlaylist(
                            id = it.playlist.id,
                            items = it.videos
                                .map { video ->
                                    DataItem.Video(
                                        url = video.url,
                                        name = video.name,
                                        thumbnail = video.thumbnail,
                                        streamType = video.streamType,
                                        channelUrl = video.channelUrl ?: "",
                                        channelName = video.channelName,
                                        shortDescription = "",
                                        uploadDate = video.uploadDate,
                                        uploadOffset = video.uploadDate,
                                        viewCount = video.viewCount,
                                        isShort = false,
                                        duration = video.duration,
                                        channelAvatars = "",
                                        channelVerified = video.isChannelVerified,
                                        playlistId = playlist.id,
                                    )
                                }.toMutableStateList(),
                            metadata = PlaylistMetadata(
                                name = it.playlist.name,
                                channelUrl = it.playlist.channelUrl,
                                isChannelVerified = it.playlist.isChannelVerified,
                                count = it.playlist.count,
                                channelName = it.playlist.channelName,
                            )
                        )
                    } ?: throw LocalPlaylistNotFoundException()
                }
                
                is DataItem.Playlist.OnlinePlaylist -> {
                    emit(PlaylistExtractor.fetchPlaylist(playlist.url))
                }
                
                is DataItem.Playlist.OfflineFirstPlaylist -> {
                    // todo implement syncing strategy // check for internet connection// check both sources concurrently
                }
            }.let { emit(it) }
        }.asResult(Dispatchers.IO)
    
}
