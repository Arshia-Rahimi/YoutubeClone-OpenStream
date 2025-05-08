package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.github.openstream.core.database.entities.PlaylistEntity
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

    @Insert
    suspend fun insert(vararg playlistEntities: PlaylistEntity): List<Long>
    
    @Upsert
    suspend fun upsert(vararg playlistEntities: PlaylistEntity)

    @Delete
    suspend fun delete(vararg playlistEntities: PlaylistEntity)

    @Query("UPDATE $TABLE_NAME SET thumbnail = :thumbnail WHERE playlistId = :id")
    suspend fun updatePlaylistThumbnail(id: Long, thumbnail: String)
}
