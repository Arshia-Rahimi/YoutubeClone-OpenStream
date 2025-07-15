package com.github.openstream.core.data

import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.Success
import com.github.openstream.core.shared.extractor.data.VideoData
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun fetchVideo(url: String): Flow<Resource<VideoData>>

    fun deleteLocalVideoHistory(): Flow<Resource<Success>>
}
