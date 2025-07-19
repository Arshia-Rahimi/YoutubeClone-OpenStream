package com.github.openstream.core.data.impl

import android.content.Context
import coil3.imageLoader
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.common.util.asResult
import com.github.openstream.core.data.CacheRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

class CoilCacheRepository(
    private val context: Context,
): CacheRepository {
    
    override fun clearDiskImageCache() = flow { 
        val imageLoader = context.imageLoader
        imageLoader.diskCache?.clear()
        emit(Success)
    }.asResult(Dispatchers.IO)
}
