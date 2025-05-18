package com.github.arshiarahimi.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.github.arshiarahimi.openstream.core.database.entities.VideoEntity

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos WHERE videoId = :videoId")
    suspend fun get(videoId: Long): VideoEntity


    @Query("SELECT * FROM videos WHERE url = :url")
    suspend fun get(url: String): VideoEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg videoEntity: VideoEntity): List<Long>

    @Update
    suspend fun update(vararg videoEntity: VideoEntity)

    @Transaction
    suspend fun upsertAndReturnIds(vararg videoEntity: VideoEntity): List<Long> =
        buildList {
            videoEntity.forEach {
                val id = insert(it).first()
                if (id == -1L) {
                    update(it)
                    add(it.videoId)
                } else add(id)
            }
        }

    @Upsert
    suspend fun upsert(vararg videoEntities: VideoEntity)

    @Delete
    suspend fun delete(vararg videoEntities: VideoEntity)
}
