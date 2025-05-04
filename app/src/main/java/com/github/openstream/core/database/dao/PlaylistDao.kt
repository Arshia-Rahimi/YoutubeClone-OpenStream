package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.PlaylistVideoCrossRef
import com.github.openstream.core.database.entities.relationships.PlaylistWithVideos
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    companion object {
        const val TABLE_NAME = "playlists"
    }

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id")
    fun indexFlow(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id")
    fun index(): List<PlaylistEntity>

    @Insert
    suspend fun insert(vararg playlistEntities: PlaylistEntity): List<Long>
    
    @Upsert
    suspend fun upsert(vararg playlistEntities: PlaylistEntity)

    @Delete
    suspend fun delete(vararg playlistEntities: PlaylistEntity)
    
    @Transaction
    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getPlaylistWithVideos(id: Long): PlaylistWithVideos?
    
    @Insert
    suspend fun addToPlaylist(vararg playlistVideoCrossRef: PlaylistVideoCrossRef)
}
