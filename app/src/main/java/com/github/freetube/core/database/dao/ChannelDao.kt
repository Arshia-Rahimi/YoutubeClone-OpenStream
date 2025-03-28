package com.github.freetube.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.github.freetube.core.database.entities.ChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {
    companion object {
        const val TABLE_NAME = "channels"
    }

    @Query("SELECT * FROM $TABLE_NAME ORDER BY name")
    fun index(): Flow<List<ChannelEntity>>

    @Upsert
    suspend fun upsert(vararg channelEntities: ChannelEntity)

    @Delete
    suspend fun delete(vararg channelEntities: ChannelEntity)
}
