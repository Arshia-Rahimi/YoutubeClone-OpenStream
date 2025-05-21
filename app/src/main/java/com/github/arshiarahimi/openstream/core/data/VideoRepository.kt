package com.github.arshiarahimi.openstream.core.data

import androidx.media3.common.MediaItem
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.common.util.Success
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun fetchVideo(url: String): Flow<Resource<MediaItem>>

    suspend fun deleteLocalVideoHistory(): Flow<Resource<Success>>
}
