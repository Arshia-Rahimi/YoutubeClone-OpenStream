package com.github.freetube.core.data.imp

import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.common.util.Success
import com.github.freetube.core.common.util.asResult
import com.github.freetube.core.data.PlaylistsRepository
import com.github.freetube.core.database.LibreTubeDatabase
import com.github.freetube.core.database.entities.PlaylistEntity
import com.github.freetube.core.extractor.model.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RoomPlaylistsRepository(
    private val db: LibreTubeDatabase,
) : PlaylistsRepository {
    override val playlists = db.playlistDao().index().map {
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
            db.playlistDao().delete(playlist.toEntity() as PlaylistEntity)
            emit(Success)
        }.asResult(Dispatchers.IO)

    override suspend fun addToPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int
    ): Flow<Resource<Success>> =
        flow {
            // todo
            emit(Success)
        }.asResult(Dispatchers.IO)

    override suspend fun removeFromPlaylist(
        videos: List<DataItem.Video>,
        playlistId: Int
    ): Flow<Resource<Success>> = flow {
        // todo
        emit(Success)
    }.asResult(Dispatchers.IO)
}
