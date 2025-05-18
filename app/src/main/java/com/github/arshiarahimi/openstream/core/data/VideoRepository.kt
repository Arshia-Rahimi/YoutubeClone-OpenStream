package com.github.arshiarahimi.openstream.core.data

import androidx.media3.common.MediaItem
import com.github.arshiarahimi.openstream.core.common.util.Resource
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun fetchVideo(url: String): Flow<Resource<MediaItem>>
}
