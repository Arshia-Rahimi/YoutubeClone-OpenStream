package com.github.openstream.core.shared

// mini player config
const val MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO = 0.3f
const val MINI_PLAYER_CONTENT_VISIBILITY_THRESHOLD = 1f
const val VIDEO_PROGRESS_INDICATOR_THICKNESS = 2

object KoinQualifiers {
    const val PREFERENCES = "preferences"
    const val PLAYER_CONFIG = "player_config"
    const val QUEUE = "queue"
}

object DefaultPlaylists {
    val all = arrayOf(WATCH_LATER_ID, LIKED_VIDEOS_ID)

    const val WATCH_LATER_ID = 0L
    const val LIKED_VIDEOS_ID = 1L
}
