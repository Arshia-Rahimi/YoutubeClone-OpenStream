package com.github.arshiarahimi.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.github.arshiarahimi.openstream.core.database.entities.relationships.QueueItem
import kotlinx.coroutines.flow.Flow

@Dao
interface QueueItemDao {

    @Transaction
    @Query("SELECT * FROM videos")
    fun index(): Flow<List<QueueItem>>

    @Insert
    fun insert(vararg queueItem: QueueItem)

    @Update
    fun update(vararg queueItem: QueueItem)

    @Query("DELETE FROM queue_order WHERE videoId = :videoId")
    fun removeFromQueue(videoId: Long)
}
