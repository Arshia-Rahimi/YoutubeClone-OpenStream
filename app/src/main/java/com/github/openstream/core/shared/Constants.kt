package com.github.openstream.core.shared

object MiniPlayerConfig {
    const val WIDTH_TO_SCREEN_WIDTH_RATIO = 0.3f
    const val LANDSCAPE_WIDTH_TO_SCREEN_WIDTH_RATIO = 0.15f
    const val CONTENT_VISIBILITY_THRESHOLD = 1f
}

object KoinQualifiers {
    const val PREFERENCES = "preferences"
}

object DefaultPlaylists {
    val all = arrayOf(WATCH_LATER_ID, LIKED_VIDEOS_ID, HISTORY_ID)

    const val HISTORY_ID = 0L
    const val LIKED_VIDEOS_ID = 1L
    const val WATCH_LATER_ID = 2L
}

const val LOG_FILENAME = "openstream-log.txt"
