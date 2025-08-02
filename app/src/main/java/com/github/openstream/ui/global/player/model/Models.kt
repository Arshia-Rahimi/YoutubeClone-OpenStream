package com.github.openstream.ui.global.player.model

data class VideoLocalState(
    val isInWatchLater: Boolean = false,
    val isLiked: Boolean = false,
    val channelId: Long? = null,
)

enum class PlayerSheetState {
    MINI_PLAYER, EXPANDED,
}

enum class PlaybackSpeed(
    val value: Float,
    val string: String,
) {
    X_0_25(0.25f, "0.25"),
    X_0_5(0.5f, "0.5"),
    X_0_75(0.75f, "0.75"),
    X_1(1f, "1"),
    X_1_25(1.25f, "1.25"),
    X_1_5(1.5f, "1.5"),
    X_1_75(1.75f, "1.75"),
    X_2(2f, "2")
}
