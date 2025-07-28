package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import kotlinx.coroutines.flow.Flow

interface CacheRepository {
    fun deleteLocalVideoHistory(): Flow<Resource<Success>>
    
    fun clearDiskImageCache(): Flow<Resource<Success>>
    
    fun clearAllCache(): Flow<Resource<Success>>
}
