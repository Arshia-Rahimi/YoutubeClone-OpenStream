package com.github.openstream.core.data.impl

import android.content.Context
import com.github.openstream.core.common.android.restartApp
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.common.util.async
import com.github.openstream.core.common.util.asyncList
import com.github.openstream.core.common.util.getLogger
import com.github.openstream.core.data.CacheRepository
import com.github.openstream.core.database.OpenStreamDatabase
import com.github.openstream.core.shared.DefaultPlaylists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope

class OpenStreamCacheRepository(
    private val db: OpenStreamDatabase,
    private val context: Context,
) : CacheRepository {
    
    override fun deleteLocalVideoHistory(): Flow<Resource<Success>> = flow {
        supervisorScope {
            asyncList {
                async { db.videoDao().deleteAll() }
                async { db.playlistDao().deleteAllVideos() }
                async { db.channelDao().deleteAllVideos() }
            }.awaitAll()
            emit(Success)
        }
    }.asResult(Dispatchers.IO, this::class.simpleName, "deleteLocalVideoHistory")
    
    override fun clearAllCache(): Flow<Resource<Success>> = flow {
        supervisorScope {
            asyncList {
                async { context.deleteDatabase(OpenStreamDatabase.NAME) }
                async { getLogger().clearLog() }
            }.awaitAll()
            emit(Success)
            context.restartApp()
        }
    }.asResult(Dispatchers.IO)
    
    override fun clearWatchHistory(): Flow<Resource<Success>> = flow {
        supervisorScope {
            asyncList {
                async { db.playlistDao().deletePlaylistVideos(DefaultPlaylists.HISTORY_ID) }
                async { db.videoDao().clearWatchHistory() }
                async { db.playlistDao().clearWatchHistory() }
            }.awaitAll()
            emit(Success)
        }
    }.asResult(Dispatchers.IO, this::class.simpleName, "clear watch history")
}
