package com.github.arshiarahimi.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.github.arshiarahimi.openstream.core.database.entities.PlaylistEntity
import com.github.arshiarahimi.openstream.core.database.entities.crossrefs.PlaylistVideoCrossRef
import com.github.arshiarahimi.openstream.core.database.entities.relationships.PlaylistWithVideos
import com.github.arshiarahimi.openstream.core.database.entities.relationships.VideoWithPlaylists
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    companion object {
        const val TABLE_NAME = "playlists"
    }

    @Query("SELECT * FROM $TABLE_NAME ORDER BY playlistId")
    fun indexFlow(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY playlistId")
    fun index(): List<PlaylistEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE playlistId = :playlistId")
    fun get(playlistId: Long): PlaylistEntity?

    @Query("SELECT * FROM $TABLE_NAME WHERE url = :url")
    fun get(url: String): PlaylistEntity?

    @Insert
    suspend fun insert(vararg playlistEntities: PlaylistEntity): List<Long>

    @Upsert
    suspend fun upsert(vararg playlistEntities: PlaylistEntity)

    @Delete
    suspend fun delete(vararg playlistEntities: PlaylistEntity)

    @Query("UPDATE $TABLE_NAME SET thumbnail = :thumbnail WHERE playlistId = :id")
    suspend fun updatePlaylistThumbnail(id: Long, thumbnail: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToPlaylist(vararg playlistVideoCrossRef: PlaylistVideoCrossRef)

    @Delete
    suspend fun removeFromPlaylist(vararg playlistVideoCrossRef: PlaylistVideoCrossRef)

    @Transaction
    @Query("SELECT * FROM videos WHERE videoId = :id")
    suspend fun getVideoWithPlaylists(id: Long): VideoWithPlaylists?

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :id")
    suspend fun getPlaylistWithVideos(id: Long): PlaylistWithVideos?

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :id")
    fun getPlaylistWithVideosFlow(id: Long): Flow<PlaylistWithVideos?>

    @Query("DELETE FROM playlist_video")
    suspend fun deleteAllVideos()
    
    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM playlist_video
            WHERE playlistId = :playlistId AND videoId = :videoId
        )
    """
    )
    fun isInPlaylist(videoId: Long, playlistId: Long): Flow<Boolean>
    
}
