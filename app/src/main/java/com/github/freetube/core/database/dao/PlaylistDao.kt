package com.github.freetube.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.github.freetube.core.database.entities.PlaylistEntity
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
}
