package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.crossrefs.PlaylistVideoCrossRef
import com.github.openstream.core.database.entities.relationships.PlaylistWithVideos
import com.github.openstream.core.database.entities.relationships.PlaylistWithVideosWithPivot
import com.github.openstream.core.database.entities.relationships.VideoWithPlaylists
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
    
    @Query("SELECT * FROM $TABLE_NAME WHERE playlistId = :playlistId")
    fun getAsFlow(playlistId: Long): Flow<PlaylistEntity?>
    
    @Insert
    suspend fun insert(vararg playlistEntities: PlaylistEntity): List<Long>
    
    @Upsert
    suspend fun upsert(vararg playlistEntities: PlaylistEntity)
    
    @Delete
    suspend fun delete(vararg playlistEntities: PlaylistEntity)
    
    @Query("UPDATE $TABLE_NAME SET thumbnail = :thumbnail WHERE playlistId = :id")
    suspend fun updatePlaylistThumbnail(id: Long, thumbnail: String?)
    
    @Query("UPDATE $TABLE_NAME SET count = :count WHERE playlistId = :id")
    suspend fun updatePlaylistCount(id: Long, count: Long)
    
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
    
    @Query(
        """
    SELECT
        p.*,
    
        v.videoId AS video_videoId,
        v.name AS video_name,
        v.url AS video_url,
        v.channel_name AS video_channel_name,
        v.thumbnail AS video_thumbnail,
        v.view_count AS video_view_count,
        v.upload_date AS video_upload_date,
        v.stream_type AS video_stream_type,
        v.duration AS video_duration,
        v.channel_url AS video_channel_url,
        v.is_channel_verified AS video_is_channel_verified,
        v.position AS video_position,

        pv.playlistId AS pivot_playlistId,
        pv.videoId AS pivot_videoId,
        pv.timestamp AS pivot_timestamp
    FROM playlists p
    INNER JOIN playlist_video pv ON p.playlistId = pv.playlistId
    INNER JOIN videos v ON v.videoId = pv.videoId
    WHERE p.playlistId = :id
    ORDER BY pv.timestamp DESC"""
    )
    fun getPlaylistWithVideosFlowSorted(id: Long): Flow<List<PlaylistWithVideosWithPivot?>>
    
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
