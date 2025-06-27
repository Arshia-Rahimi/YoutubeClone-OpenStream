package com.github.openstream.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.openstream.core.database.dao.ChannelDao
import com.github.openstream.core.database.dao.PlaylistDao
import com.github.openstream.core.database.dao.VideoDao
import com.github.openstream.core.database.entities.ChannelEntity
import com.github.openstream.core.database.entities.PlaylistEntity
import com.github.openstream.core.database.entities.VideoEntity
import com.github.openstream.core.database.entities.crossrefs.ChannelVideoCrossRef
import com.github.openstream.core.database.entities.crossrefs.PlaylistVideoCrossRef

@Database(
    entities = [
        ChannelEntity::class,
        PlaylistEntity::class,
        VideoEntity::class,
        PlaylistVideoCrossRef::class,
        ChannelVideoCrossRef::class,
    ],
    version = 1,
)
@TypeConverters(OpenStreamRoomConverter::class)
abstract class OpenStreamDatabase : RoomDatabase() {
    companion object {
        const val NAME = "OpenStream_DB"
    }

    abstract fun channelDao(): ChannelDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun videoDao(): VideoDao
}
