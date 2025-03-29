package com.github.freetube.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.freetube.core.database.dao.ChannelDao
import com.github.freetube.core.database.dao.PlaylistDao
import com.github.freetube.core.database.dao.VideoDao
import com.github.freetube.core.database.entities.ChannelEntity
import com.github.freetube.core.database.entities.PlaylistEntity
import com.github.freetube.core.database.entities.VideoEntity

@Database(
    entities = [
        ChannelEntity::class,
        PlaylistEntity::class,
        VideoEntity::class
    ],
    version = 1,
)
abstract class LibreTubeDatabase : RoomDatabase() {
    companion object {
        const val NAME = "LibreTube_DB"
    }

    abstract fun channelDao(): ChannelDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun videoDao(): VideoDao
}
