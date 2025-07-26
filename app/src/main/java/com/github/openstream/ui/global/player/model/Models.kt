package com.github.openstream.ui.global.player.model

data class VideoLocalState(
    val isInWatchLater: Boolean = false,
    val isLiked: Boolean = false,
    val channelId: Long? = null,
)

enum class PlayerSheetState {
    MINI_PLAYER, EXPANDED,
}
