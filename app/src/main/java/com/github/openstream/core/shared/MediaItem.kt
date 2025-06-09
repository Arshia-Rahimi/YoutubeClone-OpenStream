package com.github.openstream.core.shared

import androidx.media3.common.MediaItem
import com.github.openstream.core.model.extractordata.VideoData

fun MediaItem.getVideoData() = localConfiguration?.tag as VideoData
