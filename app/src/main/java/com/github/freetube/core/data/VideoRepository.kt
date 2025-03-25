package com.github.freetube.core.data

import androidx.media3.common.MediaItem
import com.github.freetube.core.common.util.Resource
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    suspend fun fetchVideo(url: String): Flow<Resource<MediaItem>>
}
