package com.github.openstream.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.openstream.core.database.dao.ChannelDao
import com.github.openstream.core.database.dao.PlaylistDao
import com.github.openstream.core.database.dao.VideoDao
import com.github.openstream.core.database.entities.ChannelEntity
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.VideoEntity

@Database(
    entities = [
        ChannelEntity::class,
        PlaylistEntity::class,
        VideoEntity::class
    ],
    version = 1,
)
abstract class OpenStreamDatabase : RoomDatabase() {
    companion object {
        const val NAME = "OpenStream_DB"
    }

    abstract fun channelDao(): ChannelDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun videoDao(): VideoDao
}
