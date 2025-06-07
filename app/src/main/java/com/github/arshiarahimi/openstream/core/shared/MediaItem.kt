package com.github.arshiarahimi.openstream.core.shared

import androidx.media3.common.MediaItem
import com.github.arshiarahimi.openstream.core.model.extractordata.VideoData

fun MediaItem.getVideoData() = localConfiguration?.tag as VideoData