package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.github.openstream.core.database.entities.VideoEntity

@Dao
interface VideoDao {
    
    @Query("SELECT * FROM videos WHERE videoId = :videoId")
    suspend fun get(videoId: Long): VideoEntity
    
    
    @Query("SELECT * FROM videos WHERE url = :url")
    suspend fun get(url: String): VideoEntity?
    
    @Insert
    suspend fun insert(vararg videoEntities: VideoEntity): List<Long>
    
    @Upsert
    suspend fun upsert(vararg videoEntities: VideoEntity)

    @Delete
    suspend fun delete(vararg videoEntities: VideoEntity)
}
