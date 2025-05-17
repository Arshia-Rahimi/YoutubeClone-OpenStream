package com.github.openstream.core.data.impl

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.PlaylistVideoCrossRef
import com.github.openstream.core.extractor.PlaylistExtractor
import com.github.openstream.core.model.extractordata.OfflineFirstPlaylist
import com.github.openstream.core.model.extractordata.OnlinePlaylist
import com.github.openstream.core.model.extractordata.PlaylistItem
import com.github.openstream.core.model.extractordata.VideoItem
import com.github.openstream.core.model.extractordata.YoutubePlaylist
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

    // local playlists
    override fun createPlaylist(playlistName: String): Flow<Resource<Success>> = flow {
        db.playlistDao().insert(PlaylistEntity(name = playlistName, count = 0L))
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun deletePlaylist(playlist: PlaylistItem.LocalPlaylistItem): Flow<Resource<Success>> =
        flow {
            require(playlist.id != WATCH_LATER_ID)
            db.playlistDao().delete(playlist.toEntity())
            emit(Success)
        }.asResult(Dispatchers.IO)

    override fun addToPlaylist(
        videos: List<VideoItem>,
        playlist: PlaylistItem.LocalPlaylistItem,
    ): Flow<Resource<Success>> = flow {
        val playlist = db.playlistDao().get(playlist.id)
        requireNotNull(playlist) { "playlist doesn't exist" }
        require(playlist.url != null) { ("playlist isn't local") }

        val ids = db.videoDao().insert(*videos.map { it.toEntity() }.toTypedArray())
        db.playlistDao()
            .addToPlaylist(*ids.map { PlaylistVideoCrossRef(playlist.playlistId, it) }
                .toTypedArray())

        updatePlaylistThumbnail(playlist.playlistId)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun removeFromPlaylist(
        videos: List<VideoItem>,
        playlist: PlaylistItem.LocalPlaylistItem,
    ): Flow<Resource<Success>> = flow {
        db.videoDao().delete(*videos.map { it.toEntity() }.toTypedArray())
        updatePlaylistThumbnail(playlist.id)
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
                }
            }.awaitAll()
            emit(Success)
        }
    }.asResult(Dispatchers.IO)
    
    override fun getPlaylistSavedVideos(playlist: PlaylistItem.LocalPlaylistItem): Flow<List<VideoItem>> =
        db.playlistDao().getPlaylistWithVideosFlow(playlist.id)
            ?.map { it.videos.map { it.toDataItem() } }
            ?: flow { emptyList<VideoItem>() }
    //

    // youtube playlists
    override fun getPlaylist(playlist: PlaylistItem.YoutubePlaylistItem): Flow<Resource<YoutubePlaylist>> =
        flow {
            val data = PlaylistExtractor.fetchPlaylist(playlist)
            if (playlist is PlaylistItem.OfflineFirstPlaylistItem) {
                db.playlistDao().upsert(data.data.toEntity().copy(playlistId = playlist.id))
            }
            emit(data)
        }.asResult(Dispatchers.IO)
    //
    
    // offline first playlists
    override fun getPlaylistFirstPage(playlist: OfflineFirstPlaylist): Flow<Resource<Success>> =
        flow {
            val firstPage = PlaylistExtractor.fetchFirstPage(playlist)
            db.videoDao().upsert(*firstPage.map { it.toEntity() }.toTypedArray())
            emit(Success)
        }.asResult(Dispatchers.IO)

    override fun getNextPage(playlist: OfflineFirstPlaylist): Flow<Resource<Success>> =
        flow {
            val nextPage = PlaylistExtractor.fetchNextPage(playlist)
            db.videoDao().upsert(*nextPage.map { it.toEntity() }.toTypedArray())
            emit(Success)
        }.asResult(Dispatchers.IO)
    //

    // online playlists
    override fun savePlaylist(playlist: PlaylistItem.OnlinePlaylistItem): Flow<Resource<Success>> =
        flow {
            require(db.playlistDao().index().any { it.url == playlist.url })
            { "This playlist already exist in your library" }

            val id = db.playlistDao().insert(playlist.toEntity()).first()
            updatePlaylistThumbnail(id)
            emit(Success)
        }.asResult(Dispatchers.IO)

    override fun getPlaylistFirstPage(playlist: OnlinePlaylist): Flow<Resource<List<VideoItem>>> =
        flow {
            emit(PlaylistExtractor.fetchFirstPage(playlist))
        }.asResult(Dispatchers.IO)

    override fun getNextPage(playlist: OnlinePlaylist): Flow<Resource<List<VideoItem>>> =
        flow {
            emit(PlaylistExtractor.fetchNextPage(playlist))
        }.asResult(Dispatchers.IO)
    //

    private suspend fun updatePlaylistThumbnail(playlistId: Long) {
        val latestVideoThumbnail = db.playlistDao().getPlaylistWithVideos(playlistId)
            ?.videos?.sortedBy { it.uploadDate }?.firstNotNullOf { it.thumbnail } ?: return

        db.playlistDao().updatePlaylistThumbnail(playlistId, latestVideoThumbnail)
    }

}
