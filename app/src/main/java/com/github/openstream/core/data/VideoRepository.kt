package com.github.openstream.core.data

import androidx.media3.common.MediaItem
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    suspend fun fetchVideo(url: String): Resource<MediaItem>

    fun deleteLocalVideoHistory(): Flow<Resource<Success>>
}
