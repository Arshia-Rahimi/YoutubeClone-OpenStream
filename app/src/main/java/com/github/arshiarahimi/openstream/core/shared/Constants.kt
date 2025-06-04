package com.github.arshiarahimi.openstream.core.shared

// mini player config
const val MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO = 0.3f
const val MINI_PLAYER_CONTENT_VISIBILITY_THRESHOLD = 1f
const val VIDEO_PROGRESS_INDICATOR_THICKNESS = 2

// koin qualifiers
const val PREFERENCES_QUALIFIER = "preferences"
const val PLAYER_CONFIG_QUALIFIER = "player_config"

object DefaultPlaylists {
    val all = arrayOf(WATCH_LATER_ID, LIKED_VIDEOS_ID)

    const val WATCH_LATER_ID = 0L
    const val LIKED_VIDEOS_ID = 1L
}
