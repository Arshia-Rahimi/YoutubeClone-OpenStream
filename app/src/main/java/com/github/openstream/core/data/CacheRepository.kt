package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import kotlinx.coroutines.flow.Flow

interface CacheRepository {
    fun clearDiskImageCache(): Flow<Resource<Success>>
}
