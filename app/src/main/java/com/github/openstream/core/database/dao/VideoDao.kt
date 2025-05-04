package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.VideoEntity
import com.github.openstream.core.database.entities.relationships.VideoWithPlaylists
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    companion object {
        const val TABLE_NAME = "videos"
    }
    
    @Insert
    suspend fun insert(vararg videoEntities: VideoEntity): List<Long>
    
    @Upsert
    suspend fun upsert(vararg videoEntities: VideoEntity)

    @Delete
    suspend fun delete(vararg videoEntities: VideoEntity)
    
    @Transaction
    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getVideoWithPlaylists(id: Long): VideoWithPlaylists?
}
