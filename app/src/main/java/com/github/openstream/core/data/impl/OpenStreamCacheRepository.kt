package com.github.openstream.core.data.impl

import android.content.Context
import coil3.imageLoader
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.CacheRepository
import com.github.openstream.core.database.OpenStreamDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope

class OpenStreamCacheRepository(
    private val db: OpenStreamDatabase,
    private val context: Context,
): CacheRepository {
    
    override fun deleteLocalVideoHistory(): Flow<Resource<Success>> =
        flow {
            supervisorScope {
                val d1 = async { db.videoDao().deleteAll() }
                val d2 = async { db.playlistDao().deleteAllVideos() }
                val d3 = async { db.channelDao().deleteAllVideos() }
                d1.await()
                d2.await()
                d3.await()
                emit(Success)
            }
        }.asResult(Dispatchers.IO, this::class.simpleName, "deleteLocalVideoHistory()")
    
    
    override fun clearDiskImageCache() = flow { 
        val imageLoader = context.imageLoader
        imageLoader.diskCache?.clear()
        emit(Success)
    }.asResult(Dispatchers.IO, this::class.simpleName, "clearDiskImageCache()")
    
    override fun clearAllCache(): Flow<Resource<Success>> = flow {
        supervisorScope {
            val d1 = async { db.clearAllTables() }
            val d2 = async {
                val imageLoader = context.imageLoader
                imageLoader.diskCache?.clear()
                imageLoader.memoryCache?.clear()
            }
            d1.await()
            d2.await()
            emit(Success)
        }
    }.asResult(Dispatchers.IO, this::class.simpleName, "clearAllCache()")
    
}
