package com.github.openstream.core.database.di

import androidx.room.Room
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.database.entities.PlaylistEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            OpenStreamDatabase::class.java,
            OpenStreamDatabase.NAME,
        ).build()
            .also {
                val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
                scope.launch {
                    if (it.playlistDao().indexFlow().first().none { it.name == "watch later" }) {
                        it.playlistDao().upsert(
                            PlaylistEntity(
                                name = "watch later",
                                channelUrl = "",
                                isChannelVerified = false,
                                count = 0,
                                channelName = "",
                                id = 0,
                            )
                        )
                    }
                }
            }
    }
}
