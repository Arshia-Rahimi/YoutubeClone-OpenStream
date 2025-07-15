package com.github.openstream.core.data.impl

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.crossrefs.PlaylistVideoCrossRef
import com.github.openstream.core.extractor.datasource.PlaylistRemoteDataSource
import com.github.openstream.core.shared.DefaultPlaylists
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.OfflineFirstPlaylistExtractor
import com.github.openstream.core.shared.extractor.OnlinePlaylistExtractor
import com.github.openstream.core.shared.extractor.PlaylistExtractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
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

    // local playlists
    override fun getPlaylistItem(playlistId: Long) =
        db.playlistDao().getAsFlow(playlistId).mapNotNull { it?.toDataItem() }

    override fun createPlaylist(playlistName: String): Flow<Resource<Success>> = flow {
        db.playlistDao().insert(PlaylistEntity(name = playlistName, count = 0L))
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun deletePlaylist(playlist: PlaylistItem.LocalPlaylistItem): Flow<Resource<Success>> =
        flow {
            require(playlist.id !in DefaultPlaylists.all)
            db.playlistDao().delete(playlist.toEntity())
            emit(Success)
        }.asResult(Dispatchers.IO)

    override fun addToPlaylist(
        videos: List<VideoItem>,
        playlistId: Long,
    ): Flow<Resource<Success>> = flow {
        val playlist = db.playlistDao().get(playlistId)
        requireNotNull(playlist) { "playlist doesn't exist" }
        require(playlist.url == null) { ("playlist isn't local") }

        val (localVideos, onlineVideos) = videos.partition { it.id != null }
        val onlineVideosIds =
            db.videoDao().insert(*onlineVideos.map { it.toEntity() }.toTypedArray())
        val allIds = localVideos.mapNotNull { it.id }.union(onlineVideosIds)

        db.playlistDao().addToPlaylist(
            *allIds
                .map { PlaylistVideoCrossRef(playlist.playlistId, it) }
                .toTypedArray()
        )

        updatePlaylistThumbnail(playlist.playlistId)
        updatePlaylistCount(playlist.playlistId)

        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun removeFromPlaylist(
        videos: List<VideoItem>,
        playlistId: Long,
    ): Flow<Resource<Success>> = flow {
        val playlist = db.playlistDao().get(playlistId)
        requireNotNull(playlist) { "playlist doesn't exist" }
        require(playlist.url == null) { ("playlist isn't local") }

        db.playlistDao().removeFromPlaylist(
            *videos.mapNotNull { it.id }
                .map { PlaylistVideoCrossRef(playlist.playlistId, it) }
                .toTypedArray()
        )

        updatePlaylistThumbnail(playlistId)
        updatePlaylistCount(playlistId)

        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun saveVideoToPlaylists(
        video: VideoItem,
        playlistsMap: Map<PlaylistItem.LocalOnlyPlaylistItem, Boolean>
    ): Flow<Resource<Success>> = flow {
        val videoId = video.id ?: db.videoDao().insert(video.toEntity()).first()
        val currentPlaylists =
            db.playlistDao().getVideoWithPlaylists(videoId)?.playlists?.map { it.playlistId }
                ?: emptyList()

        coroutineScope {
            playlistsMap.map { (playlist, isInPlaylist) ->
                async {
                    when {
                        isInPlaylist && playlist.id !in currentPlaylists ->
                            db.playlistDao()
                                .addToPlaylist(PlaylistVideoCrossRef(playlist.id, videoId))

                        !isInPlaylist && playlist.id in currentPlaylists ->
                            db.playlistDao()
                                .removeFromPlaylist(PlaylistVideoCrossRef(playlist.id, videoId))
                    }

                    updatePlaylistThumbnail(playlist.id)
                    updatePlaylistCount(playlist.id)
                }
            }.awaitAll()
            emit(Success)
        }
    }.asResult(Dispatchers.IO)

    override fun isInPlaylist(videoId: Long, playlistId: Long): Flow<Boolean> =
        db.playlistDao().isInPlaylist(videoId, playlistId)

    override fun getPlaylistSavedVideos(playlist: PlaylistItem.LocalPlaylistItem): Flow<List<VideoItem>?> =
        db.playlistDao().getPlaylistWithVideosFlow(playlist.id)
            .map { it?.videos?.map { it.toDataItem() } }
    //

    // youtube playlists
    override fun getPlaylist(playlist: PlaylistItem.YoutubePlaylistItem): Flow<Resource<PlaylistExtractor>> =
        flow {
            val data = PlaylistRemoteDataSource.fetchPlaylist(playlist)
            if (playlist is PlaylistItem.OfflineFirstPlaylistItem) {
                db.playlistDao().upsert(data.data.toEntity().copy(playlistId = playlist.id))
                emit(data.toOfflineFirstPlaylist(playlist.id))
                return@flow
            }
            emit(data)
        }.asResult(Dispatchers.IO)
    
    override fun getPlaylist(url: String) = flow {
        emit(PlaylistRemoteDataSource.fetchPlaylist(url))
    }.asResult(Dispatchers.IO)
    //

    // offline first playlists
    override fun getPlaylistFirstPage(playlist: OfflineFirstPlaylistExtractor): Flow<Resource<Success>> =
        flow {
            val firstPage = PlaylistRemoteDataSource.fetchFirstPage(playlist)
            val ids =
                db.videoDao().upsertAndReturnIds(*firstPage.map { it.toEntity() }.toTypedArray())
            db.playlistDao().addToPlaylist(
                *ids
                    .map {
                        PlaylistVideoCrossRef(
                            playlistId = playlist.id,
                            videoId = it
                        )
                    }
                    .toTypedArray()
            )
            emit(Success)
        }.asResult(Dispatchers.IO)

    override fun getNextPage(playlist: OfflineFirstPlaylistExtractor): Flow<Resource<Success>> =
        flow {
            val nextPage = PlaylistRemoteDataSource.fetchNextPage(playlist)
            db.videoDao().upsert(*nextPage.map { it.toEntity() }.toTypedArray())
            emit(Success)
        }.asResult(Dispatchers.IO)
    //

    // online playlists
    override fun savePlaylist(playlist: PlaylistItem.OnlinePlaylistItem): Flow<Resource<Success>> =
        flow {
            require(db.playlistDao().index().none { it.url == playlist.url })
            { "This playlist already exist in your library" }
            
            db.playlistDao().insert(playlist.toEntity())
            emit(Success)
        }.asResult(Dispatchers.IO)

    override fun getPlaylistFirstPage(playlist: OnlinePlaylistExtractor): Flow<Resource<List<VideoItem>>> =
        flow {
            emit(PlaylistRemoteDataSource.fetchFirstPage(playlist))
        }.asResult(Dispatchers.IO)

    override fun getNextPage(playlist: OnlinePlaylistExtractor): Flow<Resource<List<VideoItem>>> =
        flow {
            emit(PlaylistRemoteDataSource.fetchNextPage(playlist))
        }.asResult(Dispatchers.IO)
    //

    private suspend fun updatePlaylistThumbnail(playlistId: Long) {
        val latestVideoThumbnail = db.playlistDao().getPlaylistWithVideos(playlistId)
            ?.videos?.sortedBy { it.uploadDate }?.firstNotNullOfOrNull { it.thumbnail }

        db.playlistDao().updatePlaylistThumbnail(playlistId, latestVideoThumbnail)
    }

    private suspend fun updatePlaylistCount(playlistId: Long) {
        val playlistVideosCount = db.playlistDao().getPlaylistWithVideos(playlistId)
            ?.videos?.size ?: return

        db.playlistDao().updatePlaylistCount(playlistId, playlistVideosCount.toLong())
    }

}
