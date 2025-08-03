package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.github.openstream.core.database.entities.VideoEntity
import com.github.openstream.core.shared.StreamType

@Dao
interface VideoDao {
    
    @Query("SELECT * FROM videos")
    suspend fun index(): List<VideoEntity>

    @Query("SELECT * FROM videos WHERE videoId = :videoId")
    suspend fun get(videoId: Long): VideoEntity?

    @Query("SELECT * FROM videos WHERE url = :url")
    suspend fun get(url: String): VideoEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg videoEntity: VideoEntity): List<Long>

    @Update
    suspend fun update(vararg videoEntity: VideoEntity)

    @Query(
        """
        UPDATE videos SET 
            name = :name,
            thumbnail = :thumbnail,
            view_count = :viewCount,
            upload_date = :uploadDate,
            stream_type = :streamType,
            duration = :duration,
            channel_name = :channelName,
            channel_url = :channelUrl,
            is_channel_verified = :isChannelVerified
        WHERE videoId = :videoId
    """
    )
    suspend fun partialUpdate(
        videoId: Long,
        name: String,
        thumbnail: String?,
        viewCount: Long,
        uploadDate: Long?,
        streamType: StreamType,
        duration: Long,
        channelName: String,
        channelUrl: String,
        isChannelVerified: Boolean
    )

    @Transaction
    suspend fun upsertAndReturnIds(vararg videoEntity: VideoEntity): List<Long> =
        buildList {
            videoEntity.forEach {
                val id = insert(it).first()
                if (id == -1L) {
                    partialUpdate(
                        videoId = it.videoId,
                        name = it.name,
                        thumbnail = it.thumbnail,
                        viewCount = it.viewCount,
                        uploadDate = it.uploadDate,
                        streamType = it.streamType,
                        duration = it.duration,
                        channelName = it.channelName,
                        channelUrl = it.channelUrl,
                        isChannelVerified = it.isChannelVerified,
                    )
                    add(it.videoId)
                } else add(id)
            }
        }
    
    @Upsert
    suspend fun upsert(vararg videoEntities: VideoEntity)

    @Delete
    suspend fun delete(vararg videoEntities: VideoEntity)

    @Query("DELETE FROM videos")
    suspend fun deleteAll()
    
    @Query("UPDATE videos SET position = 0")
    suspend fun clearWatchHistory()

}
