package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.github.openstream.core.database.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Upsert
    suspend fun upsert(vararg videoEntities: VideoEntity)

    @Delete
    suspend fun delete(vararg videoEntities: VideoEntity)
}
