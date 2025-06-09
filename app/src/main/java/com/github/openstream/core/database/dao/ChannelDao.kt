package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.github.openstream.core.database.entities.ChannelEntity
import com.github.openstream.core.database.entities.VideoEntity
import com.github.openstream.core.database.entities.crossrefs.ChannelVideoCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {
    companion object {
        const val TABLE_NAME = "channels"
    }

    @Query("SELECT * FROM $TABLE_NAME ORDER BY name")
    fun index(): Flow<List<ChannelEntity>>

    @Insert
    fun insert(channelEntity: ChannelEntity): Long

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

    @Query("SELECT DISTINCT v.* FROM videos v INNER JOIN channel_video cvr ON v.videoId = cvr.videoId")
    fun getAllChannelVideos(): Flow<List<VideoEntity>>

    @Query("DELETE FROM channel_video where channelId = :channelId")
    fun deleteAllChannelVideos(channelId: Long)

    @Query("DELETE FROM channel_video")
    suspend fun deleteAllVideos()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun upsertChannelVideos(vararg channelVideos: ChannelVideoCrossRef)
}
