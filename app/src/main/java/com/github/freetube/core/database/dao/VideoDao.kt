package com.github.freetube.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.github.freetube.core.database.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    companion object {
        const val TABLE_NAME = "videos"
    }

    @Query("SELECT * FROM $TABLE_NAME WHERE playlistId = :playlistId")
    fun index(playlistId: Int): Flow<List<VideoEntity>>

    @Upsert
    suspend fun upsert(vararg videoEntities: VideoEntity)

    @Delete
    suspend fun delete(vararg videoEntities: VideoEntity)
}
