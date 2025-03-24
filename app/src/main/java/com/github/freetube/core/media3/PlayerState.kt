package com.github.freetube.core.media3

data class PlayerState(
    val playingStatus: PlayingStatus = PlayingStatus.PAUSED,
    val playerError: String? = null,
    val repeatMode: Int = PlayerRepeatMode.OFF.ordinal,
    val shuffleModeEnabled: Boolean = false,
    val playbackSpeed: Float = 1f,
)

enum class PlayingStatus {
    PLAYING, PAUSED, BUFFERING
}

enum class PlayerRepeatMode {
    OFF, ONE, ALL
}
