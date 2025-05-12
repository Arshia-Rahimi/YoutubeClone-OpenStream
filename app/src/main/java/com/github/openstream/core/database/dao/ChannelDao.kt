package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.github.openstream.core.database.entities.ChannelEntity
import com.github.openstream.core.database.entities.relationships.ChannelWithPlaylists
import com.github.openstream.core.database.entities.relationships.ChannelWithVideos
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
    
    @Query("DELETE FROM channels WHERE channelId = :channelId")
    suspend fun delete(channelId: Long)
    
    @Query("SELECT * FROM channels WHERE channelId = :id")
    suspend fun get(id: Long): ChannelEntity?
    
    @Query("SELECT * FROM channels WHERE url = :url")
    suspend fun get(url: String): ChannelEntity?
    
    @Transaction
    @Query("SELECT * FROM channels WHERE channelId = :id")
    suspend fun getChannelWithVideos(id: Long): ChannelWithVideos?
    
    @Transaction
    @Query("SELECT * FROM channels WHERE channelId = :id")
    suspend fun getChannelWithPlaylists(id: Long): ChannelWithPlaylists?
}
