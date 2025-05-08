package com.github.openstream.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.shared.WATCH_LATER_ID
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            OpenStreamDatabase::class.java,
            OpenStreamDatabase.NAME,
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL(
                    "INSERT INTO playlists (playlistId, name, channel_url, is_channel_verified, count, channel_name) VALUES ($WATCH_LATER_ID, 'watch later', '', false, 0, '')"
                )
            }
        }).build()
    }
}
