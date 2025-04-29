package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.relationships.PlaylistWithVideos
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    companion object {
        const val TABLE_NAME = "playlists"
    }

    @Query("SELECT * FROM $TABLE_NAME ORDER BY name")
    fun index(): Flow<List<PlaylistEntity>>

    @Upsert
    suspend fun upsert(vararg playlistEntities: PlaylistEntity)

    @Delete
    suspend fun delete(vararg playlistEntities: PlaylistEntity)
    
    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getPlaylistWithVideos(id: Int): PlaylistWithVideos?
    
    @Query("UPDATE playlists SET count = count + 1 WHERE id = :playlistId")
    suspend fun incrementPlaylistCount(playlistId: Int)
    
    @Query("UPDATE playlists SET count = count - 1 WHERE id = :playlistId")
    suspend fun decrementPlaylistCount(playlistId: Int)
}
